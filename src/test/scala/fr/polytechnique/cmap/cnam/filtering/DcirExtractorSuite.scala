package fr.polytechnique.cmap.cnam.filtering

import fr.polytechnique.cmap.cnam.SharedContext
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

/**
  * @author Daniel de Paula
  */
class DcirExtractorSuite extends SharedContext {

  "DcirExtractor.extract" should "return a DataFrame with the correct schema" in {
    // Given
    val path: String = "src/test/resources/expected/DCIR.parquet"
    val expected: DataFrame = sqlContext.read.parquet(path)

    // When
    val result = new DcirExtractor(sqlContext).extract(path)

    // Then
    assert(result.schema == expected.schema)
  }

  it should "return a DataFrame without lines where the value for the column BSE_PRS_NAT is 0" in {
    // Given
    val value = 0L
    val column = col("BSE_PRS_NAT")
    val path: String = "src/test/resources/expected/DCIR.parquet"

    // When
    val result = new DcirExtractor(sqlContext).extract(path)

    // Then
    assert(result.filter(column === value).count == 0L)
  }

  it should "fail if the path is invalid" in {
    // Given
    val path: String = "src/test/resources/expected/invalid_path.parquet"

    // Then
    intercept[org.apache.spark.sql.AnalysisException] {
      new DcirExtractor(sqlContext).extract(path).count
    }
  }

  "DCIRExtractor.extract" should "filter lines with quantities > upperBound" in {
    // Given
    val value = 0L
    val column = col("`ER_PHA_F.PHA_ACT_QSN`")
    val path: String = "src/test/resources/test-input/DCIR.parquet"

    // When
    val result = implicits.SourceExtractor(sqlContext).extractDcir(path,1)

    // Then
    assert(result.count == 2)
  }


}
