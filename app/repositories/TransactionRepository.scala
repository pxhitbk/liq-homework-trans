package repositories

import models.Transaction

import scala.util.Try

/**
 * Transaction repository methods to work with transaction data..
 */
trait TransactionRepository {

  /**
   * Create new transaction.
   * @param transaction new transaction
   */
  def create(transaction: Transaction): Unit

  /**
   * Read single transaction.
   * @param id transaction id
   * @return transaction if exist or None if not
   */
  def read(id: Long): Option[Transaction]

  /**
   * Retrieve list of transaction id by specific type
   * @param `type` transaction type
   * @return list of transaction id
   */
  def readByType(`type`: String): List[Long]

  /**
   * A sum of all transactions that are transitively linked by their parent_id to transaction_id.
   * @param transactionId linked transaction id
   * @return calculated amount
   */
  def sumTransactionAmounts(transactionId: Long): Double

  /**
   * Check if transaction id is exist in database.
   * @param id transaction id
   * @return true if exist, or false if not
   */
  def containsId(id: Long): Boolean
}
