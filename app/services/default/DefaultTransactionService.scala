package services.default

import javax.inject.{Inject, Singleton}

import constant.ErrorMapper
import exception.ApplicationException
import models.Transaction
import repositories.TransactionRepository
import services.TransactionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Business logic implementation for transaction services.
 */
@Singleton
class DefaultTransactionService @Inject() (transactionRepository: TransactionRepository) extends TransactionService {
  /**
   * Store new transaction.
   * @param transaction new transaction to store
   * @throws ApplicationException if transaction already exist in DB
   * @throws ApplicationException if transaction parent id is specified but doesn't exist in DB
   */
  override def save(transaction: Transaction): Future[Unit] = Future {
    transaction.parentId match {
      case Some(pid) => {
        if (!transactionRepository.containsId(pid)) {
          throw new ApplicationException(ErrorMapper.TransactionParentIdNotExist, s"Transaction parent id '$pid' is not exist")
        }
      }
      case None =>
    }
    transactionRepository.create(transaction)
  }

  override def getByType(transactionType: String): Future[List[Long]] = Future (
    transactionRepository.readByType(transactionType)
  )

  override def sumTransactionAmounts(transactionId: Long): Future[Double] = Future (
    transactionRepository.sumTransactionAmounts(transactionId)
  )

  /**
   * Get transaction by specific id.
   * @param transactionId transaction id
   * @throws ApplicationException if transaction doesn't exist
   */
  override def getById(transactionId: Long): Future[Transaction] = Future {
    transactionRepository.read(transactionId) match {
      case Some(tx) => tx
      case None => throw new ApplicationException(ErrorMapper.TransactionNotFound, s"There's no transaction with id = '$transactionId'")
    }
  }
}
