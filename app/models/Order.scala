package models

import play.api.libs.json.Json

case class Order(
                  userId: String,
                  quantityInKg: Double,
                  pricePerKg: Double,
                  transactionType: String,
                  status: Option[String]
                )

object Order {
  val TRANSACTION_SELL  = "SELL"
  val TRANSACTION_BUY   = "BUY"

  val STATUS_CANCELLED = "CANCELLED"

  implicit val format = Json.format[Order]
}

case class OrderSummaryItem(pricePerKg: Double, quantityInKg: Double)

object OrderSummaryItem {
  implicit val format = Json.format[OrderSummaryItem]
}

case class OrderSummary(buy: List[OrderSummaryItem], sell: List[OrderSummaryItem])

object OrderSummary {
  implicit val format = Json.format[OrderSummary]
}

