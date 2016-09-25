package repositories.mem


import javax.inject.Singleton

import constant.ErrorMapper
import exception.ApplicationException
import models.Transaction
import repositories.TransactionRepository

import scala.collection.concurrent.TrieMap
import scala.util.Try

/**
* Implementation of TransactionRepository to store transaction in memory.
*/
@Singleton
class TransactionRepositoryMem extends TransactionRepository {

  val transactionData = TrieMap[Long, Transaction]();

  /**
   * Create new transaction.
   * @param transaction new transaction
   */
  override def create(transaction: Transaction): Unit = {
    transactionData.putIfAbsent(transaction.id, transaction) match {
      case Some(_) => throw new ApplicationException(ErrorMapper.TransactionIdExist, s"Transaction id '${transaction.id}' is already exist")
      case None =>
    }

  }

  /**
   * Read single transaction.
   * @param id transaction id
   * @return transaction if exist or None if not
   */
  override def read(id: Long): Option[Transaction] = transactionData.get(id);

  /**
   * A sum of all transactions that are transitively linked by their parent_id to transaction_id.
   * @param transactionId linked transaction id
   * @return calculated amount
   */
  override def sumTransactionAmounts(transactionId: Long): Double = {
    val childrenAmount = ((for ((k,v) <- transactionData if v.parentId.isDefined && v.parentId.get == transactionId) yield v.amount) sum)
    transactionData(transactionId).amount + childrenAmount
  }

  /**
   * Retrieve list of transaction by specific type
   * @param `type` transaction type
   * @return list of transaction
   */
  override def readByType(`type`: String): List[Long] = {
    (for ((k,v) <- transactionData if v.`type`.toUpperCase == `type`.toUpperCase) yield k) toList
  }

  /**
   * Check if transaction id is exist in database.
   * @param id transaction id
   * @return true if exist, or false if not
   */
  override def containsId(id: Long): Boolean = transactionData.contains(id)

  private[repositories] def data = transactionData
}