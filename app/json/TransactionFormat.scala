package json

import models.Transaction
import play.api.libs.json.{JsPath, Reads, Json}
import play.api.libs.functional.syntax._

/**
 * Json format for reading/writing transaction object.
 */
object TransactionFormat {
  implicit val TransactionWrite = Json.writes[Transaction]
  implicit val TransactionRead: Reads[Transaction] = (
    (JsPath \ "id").readNullable[Long].map(id => id.getOrElse(Long.MinValue))  and
      (JsPath \ "amount").read[Double] and
      (JsPath \ "type").read[String] and
      (JsPath \ "parentId").readNullable[Long]
    )(Transaction.apply _)
}
