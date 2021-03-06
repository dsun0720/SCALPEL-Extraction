// License: BSD 3 clause

package fr.polytechnique.cmap.cnam.study.fall.fractures

import org.apache.spark.sql.Dataset
import fr.polytechnique.cmap.cnam.etl.events._
import fr.polytechnique.cmap.cnam.etl.transformers.outcomes.OutcomesTransformer
import fr.polytechnique.cmap.cnam.study.fall.codes.FractureCodes

object PublicAmbulatoryFractures extends OutcomesTransformer with FractureCodes {

  override val outcomeName: String = "public_ambulatory_fall"

  def transform(events: Dataset[Event[MedicalAct]]): Dataset[Event[Outcome]] = {
    import events.sqlContext.implicits._

    events
      .filter(isPublicAmbulatory _)
      .filter(containsNonHospitalizedCcam _)
      .map(
        event => {
          val fractureSite = BodySite.getSiteFromCode(event.value, BodySites.sites, CodeType.CCAM)
          Outcome(event.patientID, fractureSite, outcomeName, 1.0D, event.start)
        }
      )
  }

  def isPublicAmbulatory(event: Event[MedicalAct]): Boolean = {
    event.category == McoCeCcamAct.category
  }

  def containsNonHospitalizedCcam(event: Event[MedicalAct]): Boolean = {
    NonHospitalizedFracturesCcam.exists {
      code => event.value.startsWith(code)
    }
  }
}
