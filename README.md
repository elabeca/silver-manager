Silver Manager
==============

Order JSON objects should be constructed in the following manner:

```
{
  "userId": "1",
  "quantityInKg": 3.5,
  "pricePerKg": 306.00,
  "transactionType": "SELL"
}
```

There are 3 endpoints:

```
POST        /orders/register

DELETE      /orders/cancel

GET         /orders/summary
```

To run the tests:

```
sbt test
```

To run the application:

```
sbt run
```

The application will run on port 9000: http://localhost:9000