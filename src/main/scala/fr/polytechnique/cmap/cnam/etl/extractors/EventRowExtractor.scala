// License: BSD 3 clause

package fr.polytechnique.cmap.cnam.etl.extractors

import java.sql.Timestamp
import org.apache.spark.sql.Row

trait EventRowExtractor {
  self: ColumnNames =>

  def extractPatientId(r: Row): String

  def extractStart(r: Row): Timestamp

  def extractGroupId(r: Row): String = "NA"

  def extractValue(r: Row): String = "NA"

  def extractWeight(r: Row): Double = 0.0

  def extractEnd(r: Row): Option[Timestamp] = None

  def usedColumns: List[String] = List.empty
}
