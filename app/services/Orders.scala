package services

import javax.inject.Singleton

import models.{Order, OrderSummary, OrderSummaryItem}

import scala.collection.mutable.ListBuffer

trait OrdersService {
  def register(order: Order): Unit
  def cancel(order: Order): Unit
  def getAll(): OrderSummary
}

@Singleton
class Orders extends OrdersService {
  val orders: ListBuffer[Order] = ListBuffer[Order]()

  override def register(order: Order): Unit = {
    orders += order
  }

  override def cancel(order: Order): Unit = {
    val i = orders.zipWithIndex.collect {
      case (o, i) if o.userId == order.userId &&
      o.pricePerKg == order.pricePerKg &&
      o.quantityInKg == order.quantityInKg &&
      o.transactionType == order.transactionType &&
      o.status == order.status => i
    }.headOption
    if (i.isDefined) orders.update(i.get, order.copy(status = Some(Order.STATUS_CANCELLED)))
  }

  override def getAll(): OrderSummary = {
    val filteredOrders = orders
      .filter(_.status.getOrElse("") != Order.STATUS_CANCELLED)

    val buy = filteredOrders
      .filter(_.transactionType == Order.TRANSACTION_BUY)
      .groupBy[Double](_.pricePerKg)
      .map { case (p, t) => OrderSummaryItem(p, t.map(_.quantityInKg).sum) }
      .toList
      .sortWith(_.pricePerKg > _.pricePerKg)

    val sell = filteredOrders
      .filter(_.transactionType == Order.TRANSACTION_SELL)
      .groupBy[Double](_.pricePerKg)
      .map { case (p, t) => OrderSummaryItem(p, t.map(_.quantityInKg).sum) }
      .toList
      .sortWith(_.pricePerKg < _.pricePerKg)

    OrderSummary(buy, sell)
  }
}
