package fr.polytechnique.cmap.cnam.study.pioglitazone

import java.nio.file.{Files, Paths}
import com.typesafe.config.ConfigFactory
import org.scalatest.FlatSpec
import fr.polytechnique.cmap.cnam.etl.config.StudyConfig.{InputPaths, OutputPaths}

class PioglitazoneConfigSuite extends FlatSpec {

  val inputPaths = InputPaths(
    dcir = Some("src/test/resources/test-input/DCIR.parquet"),
    mco = Some("src/test/resources/test-input/MCO.parquet"),
    irBen = Some("src/test/resources/test-input/IR_BEN_R.parquet"),
    irImb = Some("src/test/resources/test-input/IR_IMB_R.parquet"),
    irPha = Some("src/test/resources/test-input/IR_PHA_R.parquet"),
    dosages = Some("src/test/resources/test-input/DOSE_PER_MOLECULE.CSV")
  )

  val outputPaths = OutputPaths(
    root = "target/test/output",
    patients = "target/test/output/patients",
    flatEvents = "target/test/output/flat_events",
    coxFeatures = "target/test/output/cox_features",
    ltsccsFeatures = "target/test/output/ltsccs_features",
    mlppFeatures = "target/test/output/mlpp_features",
    outcomes = "target/test/output/outcomes/cancer",
    exposures = "target/test/output/exposures"
  )

  "load" should "correctly load the default configuration" in {

    val expected = PioglitazoneConfig(inputPaths, outputPaths)
    val config = PioglitazoneConfig.load("", "test")

    assert(config == expected)
  }

  it should "correctly load a configuration file, falling back to the default file" in {

    // Given
    val default = PioglitazoneConfig(inputPaths, outputPaths)
    val tempPath = "test.conf"
    val configContent = """
        | input {
        |   dcir: "new/in/path"
        | }
        | output {
        |   root: "new/out/path"
        | }
        | exposures {
        |   min_purchases: 2
        |   purchases_window: 6
        | }
        | outcomes {
        |   cancer_definition: "narrow"
        | }
      """.trim.stripMargin
    pureconfig.saveConfigAsPropertyFile(ConfigFactory.parseString(configContent), Paths.get(tempPath))

    val expected = default.copy(
      input = default.input.copy(
        dcir = Some("new/in/path")
      ),
      output = default.output.copy(
        root = "new/out/path"
      ),
      exposures = default.exposures.copy(
        minPurchases = 2,
        purchasesWindow = 6
      ),
      outcomes = default.outcomes.copy(
        cancerDefinition = "narrow"
      )
    )

    // When
    val result = PioglitazoneConfig.load(tempPath, "test")

    // Then
    try {
      assert(result == expected)
    }
    finally {
      Files.delete(Paths.get(tempPath))
    }
  }
}
