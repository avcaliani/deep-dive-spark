package br.avcaliani.scala.app.validator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TransactionValidatorSpec extends AnyFlatSpec with Matchers {

  import TransactionValidator._

  "isPositiveAmount" should "accept a strictly positive amount" in {
    isPositiveAmount(500L) shouldBe true
  }
  it should "reject a negative amount" in {
    isPositiveAmount(-500L) shouldBe false
  }
  it should "reject a zero amount" in {
    isPositiveAmount(0L) shouldBe false
  }

  "isNotSelfTransfer" should "accept a transfer between two different users" in {
    isNotSelfTransfer("Homer Simpson", "Moe Szyslak") shouldBe true
  }
  it should "reject a self-transfer" in {
    isNotSelfTransfer("Bart Simpson", "Bart Simpson") shouldBe false
  }

  "isKnownUser" should "accept a user from the known cast list" in {
    isKnownUser("Ned Flanders") shouldBe true
  }
  it should "reject an unknown user" in {
    isKnownUser("Snowball (Unknown)") shouldBe false
  }

  "isFeeValid" should "accept a fee that is exactly 5% of the amount" in {
    isFeeValid(amountCents = 10000L, feeCents = 500L) shouldBe true
  }
  it should "reject a fee that is not 5% of the amount" in {
    isFeeValid(amountCents = 10000L, feeCents = 400L) shouldBe false
  }
  it should "use integer (truncating) division when computing the expected fee" in {
    isFeeValid(amountCents = 999L, feeCents = 49L) shouldBe true
    isFeeValid(amountCents = 999L, feeCents = 50L) shouldBe false
  }

}
