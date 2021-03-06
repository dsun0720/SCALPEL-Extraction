// License: BSD 3 clause

package fr.polytechnique.cmap.cnam.etl.events

import java.sql.Timestamp

trait MedicalAct extends AnyEvent with EventBuilder {

  override val category: EventCategory[MedicalAct]

  def apply(patientID: String, groupID: String, code: String, weight: Double, date: Timestamp): Event[MedicalAct] = {
    Event(patientID, category, groupID, code, weight, date, None)
  }

  def apply(patientID: String, groupID: String, code: String, date: Timestamp): Event[MedicalAct] = {
    Event(patientID, category, groupID, code, 0.0, date, None)
  }
}

object DcirAct extends MedicalAct {
  override val category: EventCategory[MedicalAct] = "dcir_act"

  object groupID {
    val PrivateAmbulatory = "private_ambulatory"
    val PublicAmbulatory = "public_ambulatory"
    val PrivateHospital = "private_hospital"
    val Liberal = "liberal"
    val DcirAct = "DCIR_act" // For legacy purpose, works with old DCIR schemas
    val Unknown = "unknown_source"
  }

}

object McoCCAMAct extends MedicalAct {
  val category: EventCategory[MedicalAct] = "mco_ccam_act"
}

object McoCIM10Act extends MedicalAct {
  val category: EventCategory[MedicalAct] = "mco_cim10_act"
}

object McoCeCcamAct extends MedicalAct {
  val category: EventCategory[MedicalAct] = "mco_ce_act"
}

object BiologyDcirAct extends MedicalAct {
  override val category: EventCategory[MedicalAct] = "dcir_biology_act"

  object groupID {
    val PrivateAmbulatory = "private_ambulatory"
    val PublicAmbulatory = "public_ambulatory"
    val PrivateHospital = "private_hospital"
    val Liberal = "liberal"
    val DcirAct = "DCIR_act" // For legacy purpose, works with old DCIR schemas
    val Unknown = "unknown_source"
  }
}

object SsrCEAct extends MedicalAct {
  val category: EventCategory[MedicalAct] = "ssr_ce_act"
}

object SsrCCAMAct extends MedicalAct {
  val category: EventCategory[MedicalAct] = "ssr_ccam_act"
}


object SsrCSARRAct extends MedicalAct {
  val category: EventCategory[MedicalAct] = "ssr_csarr_act"
}

object HadCCAMAct extends MedicalAct {
  val category: EventCategory[MedicalAct] = "had_ccam_act"
}