package models

/**
 * Transaction model.
 */
case class Transaction(id: Long, amount: Double, `type`: String, parentId: Option[Long] = None)
