package fr.polytechnique.cmap.cnam.etl.sources.data

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Column, DataFrame}
import fr.polytechnique.cmap.cnam.etl.sources.SourceManager

object DcirSource extends SourceManager {

  /** BSE_PRS_NAT: Nature de la prestation (acte de base) */
  val BSE_PRS_NAT: Column = col("BSE_PRS_NAT")

  override def sanitize(dcir: DataFrame): DataFrame = {
    dcir.where(DcirSource.BSE_PRS_NAT =!= 0)
  }
}