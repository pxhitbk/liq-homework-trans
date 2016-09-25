package constant

import play.api.mvc.Results._

/**
 * Created by hungpx on 9/25/16.
 */
object ErrorMapper {
  val TransactionIdExist = "transaction.id.exist"
  val TransactionNotFound = "transaction.notfound"
  val TransactionParentIdNotExist = "transaction.parentid.notexist"

  val ErrorStatusCode: Map[String, Status] = Map(
    TransactionIdExist -> BadRequest,
    TransactionParentIdNotExist -> BadRequest,
    TransactionNotFound -> NotFound
  )
}
