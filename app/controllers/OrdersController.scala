package controllers

import javax.inject.Inject

import models.Order
import play.api.libs.json.Json
import play.api.mvc._
import services.OrdersService

class OrdersController @Inject() (orders: OrdersService) extends Controller {

  def register = Action(parse.json) { implicit request =>
    val order = request.body.as[Order]
    orders.register(order)
    Created
  }

  def cancel = Action(parse.json) { implicit request =>
    val order = request.body.as[Order]
    orders.cancel(order)
    Accepted
  }

  def summary = Action { implicit request =>
    Ok(Json.toJson(orders.getAll()))
  }
}