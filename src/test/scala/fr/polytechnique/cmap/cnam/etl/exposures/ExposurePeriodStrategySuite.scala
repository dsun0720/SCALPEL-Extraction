package fr.polytechnique.cmap.cnam.etl.exposures

import org.scalatest.FlatSpec

class ExposurePeriodStrategySuite extends FlatSpec {

  "fromString" should "return the correct strategy from a string input" in {
    assert(ExposurePeriodStrategy.fromString("limited") == ExposurePeriodStrategy.Limited)
    assert(ExposurePeriodStrategy.fromString("unlimited") == ExposurePeriodStrategy.Unlimited)
  }
}