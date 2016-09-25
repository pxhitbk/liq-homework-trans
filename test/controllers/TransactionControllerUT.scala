package controllers

import models.Transaction
import org.scalatest.{Matchers, FunSpec}
import play.api.libs.json.{JsNumber, JsString, JsObject, JsValue}
import play.api.test.FakeRequest
import services.TransactionService
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mockito.MockitoSugar
import play.api.test.Helpers._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


class TransactionControllerUT extends FunSpec with Matchers with MockitoSugar{

  val mockTransactionService = mock[TransactionService]
  val TestController = new TransactionController(mockTransactionService)

  describe("TransactionController") {
    describe("PUT /transactionservice/transaction/:transactionId") {
      it("Should create transaction successfully and return 200 OK") {
        val transactionId = 1L
        val testTransaction = Transaction(transactionId, 10.0, "Car")
        when(mockTransactionService.save(testTransaction)).thenReturn(Future.successful(()))
        val body = generateTransactionJson(Transaction(0, 10.0, "Car"))

        val result =
          TestController.put(transactionId)(FakeRequest(PUT, s"/transactionservice/transaction/$transactionId").withJsonBody(body))

        status(result) shouldBe OK
      }

      it("Should return 400 BadRequest if malformedJson") {
        val transactionId = 1L
        val testTransaction = Transaction(transactionId, 10.0, "Car")
        val body = JsObject(Seq(
          "amount" -> JsString("NotANumber"),
          "type" -> JsString("Car")
        ))

        val result =
          TestController.put(transactionId)(FakeRequest(PUT, s"/transactionservice/transaction/$transactionId").withJsonBody(body))

        status(result) shouldBe BAD_REQUEST
      }
    }
  }

  def generateTransactionJson(transaction: Transaction): JsValue = {
    JsObject(Seq(
      "amount" -> JsNumber(transaction.amount),
      "type" -> JsString(transaction.`type`)
    ))
  }
}
