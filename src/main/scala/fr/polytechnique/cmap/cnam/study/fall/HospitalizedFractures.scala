package fr.polytechnique.cmap.cnam.study.fall

import fr.polytechnique.cmap.cnam.etl.events._
import fr.polytechnique.cmap.cnam.etl.transformers.outcomes.OutcomeTransformer
import fr.polytechnique.cmap.cnam.study.fall.codes.FractureCodes
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, SparkSession}

/*
 * The rules for this Outcome definition can be found on the following page:
 * https://datainitiative.atlassian.net/wiki/spaces/CFC/pages/61282101/General+fractures+Fall+study
 */

case class HospitalStay(patientID: String, id: String)

object HospitalizedFractures extends OutcomeTransformer with FractureCodes {

  override val outcomeName: String = "hospitalized_fall"

  def isInCodeList[T <: AnyEvent](event: Event[T], codes: Set[String]): Boolean = {
    codes.exists(event.value.startsWith)
  }

  def isFractureDiagnosis(event: Event[Diagnosis], ghmSites: List[String]): Boolean = {
    isInCodeList(event, ghmSites.toSet)
  }

  def isMainOrDASDiagnosis(event: Event[Diagnosis]): Boolean = {
    event.category == MainDiagnosis.category || event.category == AssociatedDiagnosis.category
  }

  def isBadGHM(event: Event[MedicalAct]): Boolean = {
    isInCodeList(event, CCAMExceptions)
  }


  /**
    * filters diagnosis that do not have a DP in the same hospital stay
    * and the diagnosis that relates to an incorrectGHMStay
    */
  def filterHospitalStay(
      events: Dataset[Event[Diagnosis]],
      incorrectGHMStays: Dataset[HospitalStay])
    : Dataset[Event[Diagnosis]] = {

    val spark: SparkSession = events.sparkSession
    import spark.implicits._
    val fracturesDiagnoses = events
      .groupByKey(_.groupID)
      .flatMapGroups { case (_, diagnoses) =>
        val diagnosisStream = diagnoses.toStream
        if (diagnosisStream.exists(_.category == MainDiagnosis.category)) diagnosisStream
        else Seq.empty
      }.toDF()


    val patientsToFilter = incorrectGHMStays.select("patientID")
    fracturesDiagnoses
      .join(broadcast(patientsToFilter), Seq("patientID"), "left_anti")
      .as[Event[Diagnosis]]
  }

  def transform(
      diagnoses: Dataset[Event[Diagnosis]],
      acts: Dataset[Event[MedicalAct]], ghmSites: List[BodySite]): Dataset[Event[Outcome]] = {

    import diagnoses.sqlContext.implicits._
    val ghmCodes = BodySite.extractCodesFromSites(ghmSites)
    val correctCIM10Event = diagnoses
      .filter(isMainOrDASDiagnosis _)
      .filter(diagnosis => isFractureDiagnosis(diagnosis, ghmCodes))

    val incorrectGHMStays = acts
      .filter(isBadGHM _)
      .map(event => HospitalStay(event.patientID, event.groupID))
      .distinct()

    filterHospitalStay(correctCIM10Event, incorrectGHMStays)
      .map(event => Outcome(event.patientID, BodySite.getSiteFromCode(event.value, ghmSites), outcomeName, event.start))

  }

}
