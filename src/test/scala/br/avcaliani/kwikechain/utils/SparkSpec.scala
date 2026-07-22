package br.avcaliani.kwikechain.utils

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

trait SparkSpec extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  protected val spark: SparkSession = SparkSession
    .builder()
    .master("local[1]")
    .appName(getClass.getSimpleName)
    .getOrCreate()

  override def afterAll(): Unit = spark.stop()

  // Note! Order-independent.
  // Joins/broadcasts don't guarantee row order is preserved.
  def assertDataFrameEquals(actual: DataFrame, expected: DataFrame): Unit = {
    actual.schema.fieldNames shouldBe expected.schema.fieldNames
    actual.collect().sortBy(_.toString).toSeq shouldBe expected.collect().sortBy(_.toString).toSeq
  }

}
