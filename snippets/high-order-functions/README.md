# High Order Functions

Spark array functions (`transform`, `filter`, `withField`) on a fraud transactions dataset.

## How to run

Needs JDK 21 and [uv](https://docs.astral.sh/uv/).  
The dataset is not versioned: download [Fraudulent Transactions Data](https://www.kaggle.com/datasets/chitwanmanchanda/fraudulent-transactions-data) from Kaggle and put the CSV in `data/raw/`.

```bash
cd snippets/high-order-functions/
uv run spark-submit main.py
```

The data path is relative, so run it from this folder.

## Example

Last step ("Let's Rock"), shown here on a tiny synthetic sample: fraud transactions are removed from each customer's list and `is_fraud` becomes a boolean.

```text
+-----------+--------------+--------+-------------------------------------------------+
|name_origin|n_transactions|n_frauds|transactions                                     |
+-----------+--------------+--------+-------------------------------------------------+
|CUST0001   |3             |1       |[{CASH_OUT, 50.0, false}, {PAYMENT, 10.0, false}]|
|CUST0004   |2             |1       |[{DEBIT, 15.0, false}]                           |
+-----------+--------------+--------+-------------------------------------------------+
```

## References

- [Databricks: Higher-order functions](https://docs.databricks.com/optimizations/higher-order-lambda-functions.html)
- [Databricks: Working with Nested Data Using Higher Order Functions in SQL](https://www.databricks.com/blog/2017/05/24/working-with-nested-data-using-higher-order-functions-in-sql-on-databricks.html)
- [PySpark: Column](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql/api/pyspark.sql.Column.html)
- [Kaggle: Fraudulent Transactions Data](https://www.kaggle.com/datasets/chitwanmanchanda/fraudulent-transactions-data), thanks to [Chitwan Manchanda](https://www.kaggle.com/chitwanmanchanda) for sharing it
