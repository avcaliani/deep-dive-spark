package br.avcaliani.kwikechain.pipelines

import br.avcaliani.kwikechain.utils.SparkSpec

class DailyTransactionsPipelineSpec extends SparkSpec {

  import spark.implicits._

  private val pipeline = DailyTransactionsPipeline(spark)
  private val users = Seq(
    (0, "Homer Simpson"),
    (1, "Moe Szyslak"),
    (2, "Bart Simpson")
  ).toDF("user_id", "user_name")

  "validate" should "flag each transaction scenario correctly" in {
    val scenarios = Seq(
      ("accepts a well-formed transaction", 10000L, 500L, "Homer Simpson", "Moe Szyslak", true),
      ("rejects a negative amount", -10000L, -500L, "Homer Simpson", "Moe Szyslak", false),
      ("rejects a self-transfer", 10000L, 500L, "Homer Simpson", "Homer Simpson", false),
      ("rejects an unknown user", 10000L, 500L, "Homer Simpson", "Snowball (Unknown)", false),
      ("rejects a fee that is not exactly 5%", 10000L, 400L, "Homer Simpson", "Moe Szyslak", false),
      ("truncating fee division: exact match", 999L, 49L, "Homer Simpson", "Moe Szyslak", true),
      ("truncating fee division: off by one", 999L, 50L, "Homer Simpson", "Moe Szyslak", false)
    ).toDF("scenario", "amount_cents", "fee_cents", "from_user", "to_user", "expected_valid")

    val actual = scenarios
      .transform(pipeline.validate(users))
      .select($"scenario", $"is_valid")

    assertDataFrameEquals(
      actual,
      expected = scenarios.select($"scenario", $"expected_valid".as("is_valid"))
    )
  }

}
