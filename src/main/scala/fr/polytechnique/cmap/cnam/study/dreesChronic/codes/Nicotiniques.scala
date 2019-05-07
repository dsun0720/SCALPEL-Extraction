package fr.polytechnique.cmap.cnam.study.dreesChronic.codes

import fr.polytechnique.cmap.cnam.etl.extractors.drugs.classification.{DrugClassConfig, PharmacologicalClassConfig}

object Nicotiniques extends DrugClassConfig {

  val name: String = "Nicotiniques"
  val cip13Codes: Set[String] = Set(
    "3400933467913",
    "3400933468453",
    "3400933468743",
    "3400933468804",
    "3400933469115",
    "3400933469283",
    "3400933469573",
    "3400933469634",
    "3400933470005",
    "3400933570569",
    "3400933628673",
    "3400933628963",
    "3400933629106",
    "3400933652975",
    "3400934242267",
    "3400934242328",
    "3400934376481",
    "3400934376542",
    "3400934376603",
    "3400934645624",
    "3400934645914",
    "3400934805325",
    "3400934838903",
    "3400934839153",
    "3400934932335",
    "3400934932564",
    "3400935273574",
    "3400935273635",
    "3400935273925",
    "3400935606563",
    "3400935719447",
    "3400935719508",
    "3400935719737",
    "3400935719966",
    "3400935720047",
    "3400935720108",
    "3400935720276",
    "3400935720337",
    "3400935749772",
    "3400935749833",
    "3400935752383",
    "3400935752444",
    "3400935752673",
    "3400935752734",
    "3400935881984",
    "3400935960450",
    "3400935960689",
    "3400935960801",
    "3400935961051",
    "3400936314801",
    "3400936315341",
    "3400936315631",
    "3400936316232",
    "3400936316690",
    "3400936317123",
    "3400936408982",
    "3400936447615",
    "3400936447844",
    "3400936498990",
    "3400936499072",
    "3400936499362",
    "3400936499881",
    "3400936549661",
    "3400936549722",
    "3400936550261",
    "3400936550490",
    "3400936550551",
    "3400936550902",
    "3400936696723",
    "3400936697492",
    "3400936697843",
    "3400936698444",
    "3400936741010",
    "3400936741300",
    "3400936868489",
    "3400936954458",
    "3400936956179",
    "3400936986107",
    "3400936986336",
    "3400936986916",
    "3400937013062",
    "3400937013123",
    "3400937096218",
    "3400937096966",
    "3400937097628",
    "3400937098397",
    "3400937098977",
    "3400937099578",
    "3400937100151",
    "3400937100731",
    "3400937219129",
    "3400937219358",
    "3400937219877",
    "3400937576956",
    "3400937630177",
    "3400937630528",
    "3400937630757",
    "3400937630986",
    "3400937631129",
    "3400937631297",
    "3400937631358",
    "3400937631419",
    "3400937631587",
    "3400937718097",
    "3400937718219",
    "3400937718387",
    "3400937718448",
    "3400937718509",
    "3400937718677",
    "3400937719049",
    "3400937955720",
    "3400937955898",
    "3400937955959",
    "3400937956031",
    "3400937956260",
    "3400937956321",
    "3400937956499",
    "3400937956550",
    "3400938379808",
    "3400938380118",
    "3400938380286",
    "3400938380576",
    "3400938380637",
    "3400938381009",
    "3400938728873",
    "3400938729016",
    "3400938729535",
    "3400938729993",
    "3400938730135",
    "3400938730715",
    "3400939003368",
    "3400939395227",
    "3400939395456"
  )

  /*
  list pharmacological classes
   */

  val subNicotiniques= new PharmacologicalClassConfig(
    name = "Sub_Nicotiniques",
    ATCCodes = List("N07BA*")
  )

  val pharmacologicalClasses = List(
    subNicotiniques
  )

}
