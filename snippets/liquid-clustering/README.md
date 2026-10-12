# Liquid Clustering

Delta Lake liquid clustering: compares table size and files before and after `CLUSTER BY` on airline data.

## How to run

Databricks notebook, not meant to run locally.

1. Import `lab-liquid-clustering.ipynb` into a Databricks workspace and attach it to a cluster.
2. Change the schema in the SQL cells (`anthony_caliani`) to one you own. The notebook creates it and drops its `airlines_delta*` tables.
3. Run all cells. It reads `/databricks-datasets/asa/airlines/` (22 CSVs, 1987 to 2008).

## References

- [Delta: Liquid Clustering](https://delta.io/blog/liquid-clustering/)
- [Delta: Optimize](https://delta.io/blog/delta-lake-optimize/)
- [Databricks: Delta Lake transaction log](https://www.databricks.com/blog/2019/08/21/diving-into-delta-lake-unpacking-the-transaction-log.html)
- [Databricks: Datasets](https://docs.databricks.com/aws/en/discover/databricks-datasets)
