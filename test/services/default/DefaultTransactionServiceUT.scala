package services.default

import constant.ErrorMapper
import exception.ApplicationException
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, FunSpec}
import repositories.TransactionRepository
import models.Transaction
import scala.concurrent.ExecutionContext.Implicits.global

class DefaultTransactionServiceUT extends FunSpec with Matchers with MockitoSugar {
  val mockTransactionRepository = mock[TransactionRepository]
  val testService = new DefaultTransactionService(mockTransactionRepository)

  describe("Default implementation of TransactionService") {
    describe("save(Transaction)") {
      it("Should save transaction successfully") {
        val testTransaction = Transaction(1L, 10d, "Car")
        doNothing().when(mockTransactionRepository).create(testTransaction)
        testService.save(testTransaction)

        verify(mockTransactionRepository, times(1)).create(testTransaction)
      }

      it("Should return error if parentId is specified but the parent transaction doesn't exist") {
        val testTransaction = Transaction(1L, 10d, "Car", Some(3L))

        when(mockTransactionRepository.containsId(3L)).thenReturn(false)

        val ret = testService.save(testTransaction)
        ret.onFailure {
          case ex: ApplicationException => ex.code shouldBe ErrorMapper.TransactionParentIdNotExist
        }
        verify(mockTransactionRepository, never()).create(testTransaction)
      }
    }

    describe("getById(Long)") {

    }

    describe("sumTransactionAmounts(Long)") {

    }

    describe("getByType(String)") {

    }
  }
}
