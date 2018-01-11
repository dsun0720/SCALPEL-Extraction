package fr.polytechnique.cmap.cnam.etl.extractors.acts

import java.sql.Date

import fr.polytechnique.cmap.cnam.SharedContext
import fr.polytechnique.cmap.cnam.etl.events._
import fr.polytechnique.cmap.cnam.etl.sources.Sources
import fr.polytechnique.cmap.cnam.util.functions.makeTS
import org.apache.spark.sql.functions._

class MedicalActsSuite extends SharedContext {

  "extract" should "find all medical acts in all sources" in {

    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    // Given
    val config = MedicalActsConfig(
      dcirCodes = List("ABCD123"),
      mcoCIMCodes = List("C670", "C671"),
      mcoCCAMCodes = List("AAAA123")
    )
    val sources = {
      val mco = spark.read.parquet("src/test/resources/test-input/MCO.parquet")
      val dcir = spark.read.parquet("src/test/resources/test-input/DCIR.parquet")
      new Sources(pmsiMco = Some(mco), dcir = Some(dcir))
    }
    val expected = Seq[Event[MedicalAct]](
      // DcirAct("Patient_01", "dcir", "ABCD123", null), // The dummy data contains a null value
      McoCIM10Act("Patient_02", "10000123_10000543_2006", "C671", makeTS(2005, 12, 24)),
      McoCIM10Act("Patient_02", "10000123_10000987_2006", "C670", makeTS(2005, 12, 29)),
      McoCCAMAct("Patient_02", "10000123_10000987_2006", "AAAA123", makeTS(2005, 12, 29)),
      McoCIM10Act("Patient_02", "10000123_20000123_2007", "C670", makeTS(2007, 1, 29)),
      McoCCAMAct("Patient_02", "10000123_20000123_2007", "AAAA123", makeTS(2007, 1, 29)),
      McoCIM10Act("Patient_02", "10000123_20000345_2007", "C671", makeTS(2007, 1, 29)),
      McoCIM10Act("Patient_02", "10000123_30000546_2008", "C670", makeTS(2008, 3, 8)),
      McoCCAMAct("Patient_02", "10000123_30000546_2008", "AAAA123", makeTS(2008, 3, 8)),
      McoCIM10Act("Patient_02", "10000123_30000852_2008", "C671", makeTS(2008, 3, 15))
    ).toDS

    // When
    val result = new MedicalActs(config).extract(sources)

    // Then
    sources.dcir.get.show
    assertDSs(result, expected)
  }

  "extract" should "find all medical acts in all sources, including MCO_CE" in {

    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    // Given

    val config = MedicalActsConfig(
      dcirCodes = List("ABCD123"),
      mcoCIMCodes = List("C670", "C671"),
      mcoCCAMCodes = List("AAAA123"),
      mcoCECodes = List("angi")
    )

    val mcoCE = {
      val date = new Date(makeTS(2003, 2, 1).getTime)
      Seq(
        ("george", "coloscopie", date),
        ("georgette", "angine", date)
      ).toDF("NUM_ENQ", "MCO_FMSTC__CCAM_COD", "EXE_SOI_DTD")
    }

    val sources = {
      val mco = spark.read.parquet("src/test/resources/test-input/MCO.parquet")
      val dcir = spark.read.parquet("src/test/resources/test-input/DCIR.parquet")
      new Sources(pmsiMco = Some(mco), pmsiMcoCE = Some(mcoCE), dcir = Some(dcir))
    }

    val expected = List(
      McoCIM10Act("Patient_02", "10000123_10000543_2006", "C671", makeTS(2005, 12, 24)),
      McoCIM10Act("Patient_02", "10000123_10000987_2006", "C670", makeTS(2005, 12, 29)),
      McoCCAMAct("Patient_02", "10000123_10000987_2006", "AAAA123", makeTS(2005, 12, 29)),
      McoCIM10Act("Patient_02", "10000123_20000123_2007", "C670", makeTS(2007, 1, 29)),
      McoCCAMAct("Patient_02", "10000123_20000123_2007", "AAAA123", makeTS(2007, 1, 29)),
      McoCIM10Act("Patient_02", "10000123_20000345_2007", "C671", makeTS(2007, 1, 29)),
      McoCIM10Act("Patient_02", "10000123_30000546_2008", "C670", makeTS(2008, 3, 8)),
      McoCCAMAct("Patient_02", "10000123_30000546_2008", "AAAA123", makeTS(2008, 3, 8)),
      McoCIM10Act("Patient_02", "10000123_30000852_2008", "C671", makeTS(2008, 3, 15)),
      McoCEAct("georgette", "ACE", "angine", makeTS(2003, 2, 1))
    ).toDS

    // When
    val result = new MedicalActs(config).extract(sources)

    // Then
    assertDSs(expected, result)
  }


  "extract" should "find all liberal acts in DCIR" in {
    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    // Given
    val config = MedicalActsConfig(
      dcirCodes = List("ABCD123")
    )

    val mcoCE = {
      val date = new Date(makeTS(2003, 2, 1).getTime)
      Seq(
        ("george", "coloscopie", date),
        ("georgette", "angine", date)
      ).toDF("NUM_ENQ", "MCO_FMSTC__CCAM_COD", "EXE_SOI_DTD")
    }

    val dcir = spark.read.parquet("src/test/resources/test-input/DCIR.parquet")
    val df = dcir.select("NUM_ENQ", "ER_CAM_F__CAM_PRS_IDE", "ER_ETE_F__ETE_GHS_NUM", "ER_ETE_F__ETE_TYP_COD", "EXE_SOI_DTD", "ER_ETE_F__ETE_NAT_FSJ")
      .withColumn("ER_CAM_F__CAM_PRS_IDE", when($"ER_CAM_F__CAM_PRS_IDE".isNull, "ABCD123").otherwise($"ER_CAM_F__CAM_PRS_IDE"))

    val sources = {
      val mco = spark.read.parquet("src/test/resources/test-input/MCO.parquet")
      val dcir = df
      new Sources(pmsiMco = Some(mco), pmsiMcoCE = Some(mcoCE), dcir = Some(dcir))
    }
    val expected = List(
      DcirAct("Patient_01", "liberal", "ABCD123", makeTS(2006, 1, 15) ),
      DcirAct("Patient_01", "liberal", "ABCD123", makeTS(2006, 1, 30)),
      DcirAct("Patient_02", "liberal", "ABCD123", makeTS(2006, 1, 5)),
      DcirAct("Patient_02", "liberal", "ABCD123", makeTS(2006, 1, 15)),
      DcirAct("Patient_02", "liberal", "ABCD123", makeTS(2006, 1, 30)),
      DcirAct("Patient_02", "liberal", "ABCD123", makeTS(2006, 1, 30))

    ).toDS

    // When
    val result = new MedicalActs(config).extract(sources)
    result.show()
    // Then
    assertDSs(result, expected)
  }

}
