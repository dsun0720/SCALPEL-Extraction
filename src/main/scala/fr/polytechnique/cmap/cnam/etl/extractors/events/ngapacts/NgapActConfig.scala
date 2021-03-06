// License: BSD 3 clause

package fr.polytechnique.cmap.cnam.etl.extractors.events.ngapacts

import fr.polytechnique.cmap.cnam.etl.extractors.ExtractorConfig
import fr.polytechnique.cmap.cnam.etl.extractors.codes.ExtractorCodes

/**
  * NgapActConfig defines three different ways to filter for specific ngap acts in the SNDS :
  * The base configuration is NgapActClassConfig which can filters on :
  *   - prestation type (ngapPrsNatRefs: PRS_NAT_REF),
  *   - prestation coefficient (ngapKeyLetters : PRS_NAT_CB2 or ACT_COD in the PMSI_CE),
  *   - prestation coefficient (ngapCoefficients: PRS_ACT_CFT or ACT_COE in the PMSI_CE)
  * **Note**: If acts_categories is empty, all ngap acts are extracted.
  * The Ngap acts can be found in two sources. The filtering logic differs depending on the source.
  *
  * In the Dcir, search where ngapKeyLetter is available (ie. TODO what proportion in echantillon 2008-2016):
  *   - If a list of ngapPrsNatRefs is given, it extracts all of these PrsNatRef
  *   - if a list of ngapKeyLetters and a list of ngapCoefficients is given, it extracts all combination of (keyLetter, coefficient)
  *
  * In the Pmsi (only McoCe implemented, less than 12000 ngap acts per year in SSR_CE),
  * search where ngapCoefficient is available
  *   - if a list of ngapKeyLetters and a list of ngapCoefficients is given, it extracts all combination of (keyLetter, coefficient)
  *   - if the list of ngapCoefficients is empty, extract all acts where coeff is in ngapCoefficient
  *
  * @param actsCategories List of configuration to get specific NgapActs
  */
class NgapActConfig[+C <: NgapActClassConfig](
  val actsCategories: List[C]) extends ExtractorConfig with ExtractorCodes {
  override def isEmpty: Boolean = actsCategories.isEmpty
}

object NgapActConfig {

  def apply[C <: NgapActClassConfig](actsCategories: List[C]): NgapActConfig[C] = new NgapActConfig[C](actsCategories)
}