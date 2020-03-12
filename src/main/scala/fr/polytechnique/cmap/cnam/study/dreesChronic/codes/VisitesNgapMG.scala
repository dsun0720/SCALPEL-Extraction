package fr.polytechnique.cmap.cnam.study.dreesChronic.codes

import fr.polytechnique.cmap.cnam.etl.extractors.ngapacts.NgapActClassConfig

object VisitesNgapMG extends NgapActClassConfig {
  override val ngapKeyLetters: Seq[String] = Seq(
    "TTE",
    "TDT",
    "CCX",
    "GS",
    "G",
    "C",
    "CS",
    "CA",
    "TCP",
    "TEP",
    "TLC",
    "TLE",
    "TC",
    "TCG",
    "TE1",
    "TE2",
    "VGS",
    "VG",
    "V",
    "VS",
    "VL",
    "VA",
    "VU",
    "ADA",
    "ADC",
    "ADE",
    "ATM",
    "ADI")

  val ngapCoefficients: Seq[String] = Seq()
  override val ngapPrsNatRefs: Seq[String] = Seq(
    "1096",
    "1097",
    "1105",
    "1109",
    "1110",
    "1111",
    "1112",
    "1115",
    "1117",
    "1157",
    "1158",
    "1164",
    "1165",
    "1191",
    "1192",
    "1193",
    "1194",
    "1209",
    "1210",
    "1211",
    "1212",
    "1214",
    "1221",
    "1222",
    "1323",
    "1321",
    "1324",
    "1352",
    "1351"
  )
}
