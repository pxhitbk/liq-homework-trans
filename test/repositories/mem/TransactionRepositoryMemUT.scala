package repositories.mem

import constant.ErrorMapper
import exception.ApplicationException
import models.Transaction
import org.scalatest._

class TransactionRepositoryMemUT extends FunSpec with Matchers with BeforeAndAfter {
  val repos = new TransactionRepositoryMem()

  after {
    repos.data.clear()
  }

  describe("Transaction repository in-memory storage") {
    describe("create(Transaction)") {
      it("Should create new transaction successfully") {
        repos.data.size shouldBe 0

        repos.create(Transaction(1L, 15.0, "Car"))

        repos.data.size shouldBe 1
      }

      it("Should throw ApplicationException if transaction id already exists") {
        repos.create(Transaction(1L, 15.0, "Car"))

        val ex = intercept[ApplicationException] {
          repos.create(Transaction(1L, 12.0, "Car"))
        }
        ex.code shouldBe ErrorMapper.TransactionIdExist
      }
    }

    describe("read(Long)") {
      it("Should read transaction successfully") {
        repos.read(1L) shouldBe None

        repos.data.put(1L, Transaction(1L, 15.0, "Car"))

        repos.data should contain key 1L
        repos.read(1L) should not be None
      }
    }

    describe("readByType(String)") {
      it("Should list transaction id by specific type successfully") {
        repos.data.put(1L, Transaction(1L, 15.0, "Car"))
        repos.data.put(2L, Transaction(2L, 18.0, "Bike"))
        repos.data.put(3L, Transaction(3L, 15.0, "Car"))
        repos.data.put(4L, Transaction(4L, 20.0, "Car"))
        repos.data.put(5L, Transaction(5L, 26.0, "Motor"))

        val transactionIdsByCar = repos.readByType("Car")
        transactionIdsByCar should (contain(1L) and contain(3L) and contain(4L) and have size 3)
      }
    }

    describe("sumTransactionAmounts(Long)") {
      it("Should calculate total amount of transactions linked by parent id, included parent transaction") {
        repos.data.put(1L, Transaction(1L, 150.0, "Car"))
        repos.data.put(2L, Transaction(2L, 18.0, "Bike"))
        repos.data.put(3L, Transaction(3L, 15.0, "Car", Some(1L)))
        repos.data.put(4L, Transaction(4L, 20.0, "Car"))
        repos.data.put(5L, Transaction(5L, 26.0, "Motor"))
        repos.data.put(6L, Transaction(6L, 30.0, "Car", Some(1L)))
        repos.data.put(7L, Transaction(7L, 20.0, "Car", Some(6L)))

        repos.sumTransactionAmounts(1L) shouldBe 195.0
        repos.sumTransactionAmounts(2L) shouldBe 18.0
        repos.sumTransactionAmounts(3L) shouldBe 15.0
      }
    }

    describe("containsId(Long)") {
      it("Should return true if transaction id is exist") {
        repos.data.put(1L, Transaction(1L, 150.0, "Car"))
        repos.containsId(1l) shouldBe true
      }

      it("Should return false if transaction id is not exist") {
        repos.containsId(1l) shouldBe false
      }
    }
  }
}
