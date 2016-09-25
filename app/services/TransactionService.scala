package services

import exception.ApplicationException
import models.Transaction

import scala.concurrent.Future

/**
 * Abstract methods of transaction business.
 */
trait TransactionService {
  /**
   * Store new transaction.
   * @param transaction new transaction to store
   * @throws ApplicationException if transaction already exist in DB
   * @throws ApplicationException if transaction parent id is specified but doesn't exist in DB
   */
  def save(transaction: Transaction): Future[Unit]

  /**
   * Get transaction by specific id.
   * @param transactionId transaction id
   * @throws ApplicationException if transaction doesn't exist
   */
  def getById(transactionId: Long): Future[Transaction]

  def sumTransactionAmounts(transactionId: Long): Future[Double]

  def getByType(transactionType: String): Future[List[Long]]
}
