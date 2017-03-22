package fr.polytechnique.cmap.cnam.etl.events.diagnoses

import java.sql.Timestamp
import org.apache.spark.sql.Row
import fr.polytechnique.cmap.cnam.etl.events.{AnyEvent, Event, EventCategory}

trait Diagnosis extends AnyEvent

object Diagnosis extends Diagnosis {

  val category: EventCategory[Diagnosis] = "diagnosis"

  def apply(patientID: String, code: String, date: Timestamp): Event[Diagnosis] =
    Event(patientID, category, code, 0.0, date, None)

  def fromRow(
      r: Row,
      patientIDCol: String = "patientID",
      codeCol: String = "code",
      dateCol: String = "eventDate"): Event[Diagnosis] = {

    Diagnosis(
      r.getAs[String](patientIDCol),
      r.getAs[String](codeCol),
      r.getAs[Timestamp](dateCol)
    )
  }
}