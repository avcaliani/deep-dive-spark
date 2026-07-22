package br.avcaliani.kwikechain.pipelines

import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DailyTransactionsPipelineSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  private val spark = SparkSession
    .builder()
    .master("local[1]")
    .appName("DailyTransactionsPipelineSpec")
    .getOrCreate()

  import spark.implicits._

  private val pipeline = DailyTransactionsPipeline(spark)
  private val users = Seq(
    (0, "Homer Simpson"),
    (1, "Moe Szyslak"),
    (2, "Bart Simpson")
  ).toDF("user_id", "user_name")

  private def isValid(amountCents: Long, feeCents: Long, fromUser: String, toUser: String): Boolean = {
    val df = Seq((amountCents, feeCents, fromUser, toUser))
      .toDF("amount_cents", "fee_cents", "from_user", "to_user")
    df.transform(pipeline.validate(users)).select("is_valid").as[Boolean].collect().head
  }

  "validate" should "accept a well-formed transaction" in {
    isValid(10000, 500, "Homer Simpson", "Moe Szyslak") shouldBe true
  }

  it should "reject a negative amount" in {
    isValid(-10000, -500, "Homer Simpson", "Moe Szyslak") shouldBe false
  }

  it should "reject a self-transfer" in {
    isValid(10000, 500, "Homer Simpson", "Homer Simpson") shouldBe false
  }

  it should "reject an unknown user" in {
    isValid(10000, 500, "Homer Simpson", "Snowball (Unknown)") shouldBe false
  }

  it should "reject a fee that is not exactly 5%" in {
    isValid(10000, 400, "Homer Simpson", "Moe Szyslak") shouldBe false
  }

  it should "use integer (truncating) division when computing the expected fee" in {
    isValid(999, 49, "Homer Simpson", "Moe Szyslak") shouldBe true
    isValid(999, 50, "Homer Simpson", "Moe Szyslak") shouldBe false
  }

  override def afterAll(): Unit = spark.stop()

}
