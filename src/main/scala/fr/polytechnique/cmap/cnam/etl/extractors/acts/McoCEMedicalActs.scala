package fr.polytechnique.cmap.cnam.etl.extractors.acts

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import fr.polytechnique.cmap.cnam.etl.events._
import fr.polytechnique.cmap.cnam.util.ColumnUtilities.parseTimestamp

private[acts] object McoCEMedicalActs {

  final object ColNames {
    final lazy val PatientID = "NUM_ENQ"
    final lazy val CamCode = "MCO_FMSTC__CCAM_COD"
    final lazy val Date = "ENT_DAT"
    def allCols = List(PatientID, CamCode, Date)
  }

  val outputCols = List(
    col(ColNames.PatientID).as("patientID"),
    col(ColNames.CamCode).as("code"),
    parseTimestamp(col(ColNames.Date), "ddMMyyyy").as("eventDate"),
    lit("ACE").as("groupID")
  )

  def correctCamCode(camCodes: Seq[String])(row: Row): Boolean = {
    val camCode = row.getAs[String](ColNames.CamCode)
    if (camCode != null) camCodes.map(camCode.startsWith).exists(identity) else false
  }

  def extract(mcoCE: DataFrame, ccamCodes: Seq[String]): Dataset[Event[MedicalAct]] = {
    import mcoCE.sqlContext.implicits._

    mcoCE.select(ColNames.allCols.map(col): _*)
      .filter(correctCamCode(ccamCodes) _)
      .select(outputCols: _*)
      .map(McoCEAct.fromRow(_))
  }
}
