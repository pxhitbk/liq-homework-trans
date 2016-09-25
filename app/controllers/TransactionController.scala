package controllers

import javax.inject._
import exception.ApplicationException
import play.api.libs.json._
import play.api.mvc._
import models.Transaction
import services.TransactionService
import constant.ErrorMapper._
import scala.concurrent._
import json.TransactionFormat._

/**
 * Controller exposes transaction APIs.
 */
@Singleton
class TransactionController @Inject() (transactionService: TransactionService) (implicit exec: ExecutionContext) extends Controller {

  def put(transactionId: Long) = Action.async { implicit request =>
    request.body.asJson.fold(Future.successful(BadRequest("Malformed Json")))(json =>
      json.validate[Transaction] match {
        case JsSuccess(tx, _) => {
          transactionService.save(tx.copy(id=transactionId)).map(_ => Ok("Success")).recover {
            case e: ApplicationException => ErrorStatusCode(e.code)(e.message)
          }
        }
        case e:JsError => Future.successful(BadRequest(JsError.toJson(e)))
      }
    )
  }

  def get(transactionId: Long) = Action.async (
    transactionService.getById(transactionId).map(tx => Ok(Json.toJson(tx))).recover {
      case e: ApplicationException => ErrorStatusCode(e.code)(e.message)
    }
  )

  def getByteType(transactionType: String) = Action.async (
    transactionService.getByType(transactionType).map(tx => Ok(Json.toJson(tx))).recover {
      case e: ApplicationException => ErrorStatusCode(e.code)(e.message)
    }
  )

  def getSumTransactionAmounts(transactionId: Long) = Action.async (
    transactionService.sumTransactionAmounts(transactionId).map(tx => Ok(Json.toJson(tx))).recover {
      case e: ApplicationException => ErrorStatusCode(e.code)(e.message)
    }
  )
}
