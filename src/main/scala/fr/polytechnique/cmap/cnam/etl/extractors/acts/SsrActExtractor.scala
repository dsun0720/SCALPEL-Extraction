package fr.polytechnique.cmap.cnam.etl.extractors.acts

import fr.polytechnique.cmap.cnam.etl.events._
import fr.polytechnique.cmap.cnam.etl.extractors.ssr.SsrExtractor
import org.apache.spark.sql.Row

object SsrCcamActExtractor extends SsrExtractor[MedicalAct] {
  final override val columnName: String = ColNames.CCAM
  override val eventBuilder: EventBuilder = SsrCCAMAct
}

object SsrCimMedicalActExtractor extends SsrExtractor[MedicalAct] {
  final override val columnName: String = ColNames.FP_PEC
  override val eventBuilder: EventBuilder = SsrCIM10Act
}

object SsrCsarrActExtractor extends SsrExtractor[MedicalAct] {
  final override val columnName: String = ColNames.CSARR
  override val eventBuilder: EventBuilder = SsrCSARRAct
}