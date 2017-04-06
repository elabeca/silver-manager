import models.{Order, OrderSummary, OrderSummaryItem}
import org.scalatestplus.play.PlaySpec
import services.Orders

import scala.collection.mutable.ListBuffer

class OrdersServiceSpec extends PlaySpec {

  class Setup {
    val service = new Orders()
    val orders = ListBuffer[Order](
      Order(
        userId = "1",
        quantityInKg = 55.0,
        pricePerKg = 306.00,
        transactionType = Order.TRANSACTION_BUY,
        status = None
      ),
      Order(
        userId = "2",
        quantityInKg = 44.0,
        pricePerKg = 207.00,
        transactionType = Order.TRANSACTION_SELL,
        status = None
      ),
      Order(
        userId = "3",
        quantityInKg = 23.0,
        pricePerKg = 123.00,
        transactionType = Order.TRANSACTION_SELL,
        status = None
      ),
      Order(
        userId = "5",
        quantityInKg = 11.0,
        pricePerKg = 306.00,
        transactionType = Order.TRANSACTION_BUY,
        status = None
      ),
      Order(
        userId = "6",
        quantityInKg = 20.0,
        pricePerKg = 306.00,
        transactionType = Order.TRANSACTION_SELL,
        status = None
      ),
      Order(
        userId = "7",
        quantityInKg = 5.0,
        pricePerKg = 306.00,
        transactionType = Order.TRANSACTION_BUY,
        status = None
      ),
      Order(
        userId = "5",
        quantityInKg = 37.0,
        pricePerKg = 405.00,
        transactionType = Order.TRANSACTION_BUY,
        status = None
      )
    )
  }

  "Orders service" should {

    "should return am empty list if there are no orders held in state" in new Setup {
      service.getAll() mustBe OrderSummary(List(), List())
    }

    "should return am empty list if there are only cancelled orders held in state" in new Setup {
      orders.foreach(service.register)
      orders.foreach(service.cancel)
      service.getAll() mustBe OrderSummary(List(), List())
    }

    "should return orders held in state which aren't cancelled" in new Setup {
      orders.foreach(service.register)
      service.cancel(orders(0))
      service.cancel(orders(1))
      service.cancel(orders(2))
      service.cancel(orders(3))
      service.cancel(orders(4))
      service.cancel(orders(5))
      service.getAll() mustBe OrderSummary(List(OrderSummaryItem(405, 37)), List())
    }

    "orders of the same price should be merged together even if from a different user" in new Setup {
      val buy = List(
        OrderSummaryItem(405, 37),
        OrderSummaryItem(306, 71)
      )

      val sell = List(
        OrderSummaryItem(123, 23),
        OrderSummaryItem(207, 44),
        OrderSummaryItem(306, 20)
      )

      val orderSummary = OrderSummary(buy, sell)
      orders.foreach(service.register)
      service.getAll() mustBe orderSummary
    }
  }
}
