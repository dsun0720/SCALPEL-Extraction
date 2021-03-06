// License: BSD 3 clause

package fr.polytechnique.cmap.cnam.study.bulk.extractors

import fr.polytechnique.cmap.cnam.etl.events.MedicalAct
import fr.polytechnique.cmap.cnam.etl.extractors.codes.SimpleExtractorCodes
import fr.polytechnique.cmap.cnam.etl.extractors.events.acts.SsrCeActExtractor

class SsrCeSourceExtractor(override val path: String, override val saveMode: String, override val fileFormat: String)
  extends SourceExtractor(path, saveMode, fileFormat) {
  override val sourceName: String = "SSR_CE"
  override val extractors = List(
    ExtractorSources[MedicalAct, SimpleExtractorCodes](
      SsrCeActExtractor(SimpleExtractorCodes.empty),
      List("SSR_CSTC", "SSR_FMSTC"),
      "SSR_CE_CCAM"
    )
  )
}
