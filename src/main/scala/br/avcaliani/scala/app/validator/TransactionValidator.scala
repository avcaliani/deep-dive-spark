package br.avcaliani.scala.app.validator

import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Validates DonutCoin (DONU) transactions.
 *
 * Each rule below is a small, pure, single-purpose function over plain Scala
 * values (see TransactionValidatorSpec -- no SparkSession required to test
 * them). `validate` lifts each one into a Spark UDF and applies it as a
 * DataFrame column, so there's exactly one implementation of each rule: the
 * logic under test and the logic that runs over real Spark data can never
 * drift apart.
 *
 * @param ss {@link SparkSession}.
 */
class TransactionValidator(ss: SparkSession) extends Validator {
  import TransactionValidator._

  override def validate(df: DataFrame): DataFrame = df
    .withColumn("amount_cents", col("amount_cents").cast("long"))
    .withColumn("fee_cents", col("fee_cents").cast("long"))
    .withColumn("amount_valid", isPositiveAmountUdf(col("amount_cents")))
    .withColumn("self_transfer_valid", isNotSelfTransferUdf(col("from_user"), col("to_user")))
    .withColumn("from_user_valid", isKnownUserUdf(col("from_user")))
    .withColumn("to_user_valid", isKnownUserUdf(col("to_user")))
    .withColumn("fee_valid", isFeeValidUdf(col("amount_cents"), col("fee_cents")))
    .withColumn(
      "is_valid",
      col("amount_valid") &&
        col("self_transfer_valid") &&
        col("from_user_valid") &&
        col("to_user_valid") &&
        col("fee_valid")
    )

}

object TransactionValidator {

  // Same 21-character Simpsons cast as kafka-in-docker/scripts/users.csv.
  val KNOWN_USERS: Set[String] = Set(
    "Homer Simpson", "Marge Simpson", "Bart Simpson", "Lisa Simpson",
    "Maggie Simpson", "Ned Flanders", "Milhouse Van Houten", "Mr. Burns",
    "Waylon Smithers", "Krusty the Clown", "Apu Nahasapeemapetilon",
    "Principal Skinner", "Edna Krabappel", "Chief Wiggum", "Ralph Wiggum",
    "Groundskeeper Willie", "Comic Book Guy", "Moe Szyslak", "Lenny Leonard",
    "Carl Carlson"
  )

  def isPositiveAmount(amountCents: Long): Boolean = amountCents > 0

  def isNotSelfTransfer(fromUser: String, toUser: String): Boolean = fromUser != toUser

  def isKnownUser(user: String): Boolean = KNOWN_USERS.contains(user)

  // fee_cents must equal exactly 5% of amount_cents, integer (truncating)
  // division -- matches the bash generator's arithmetic exactly.
  def isFeeValid(amountCents: Long, feeCents: Long): Boolean = feeCents == amountCents / 20

  private val isPositiveAmountUdf = udf(isPositiveAmount _)
  private val isNotSelfTransferUdf = udf(isNotSelfTransfer _)
  private val isKnownUserUdf = udf(isKnownUser _)
  private val isFeeValidUdf = udf(isFeeValid _)

}
