<img src="../.docs/spark-logo.svg" align="right" height="64px"/>

# Snippets ⚡

Small Spark experiments, one folder per exercise.  
Datasets from Kaggle are not versioned, each exercise README says where to put them.  
Run Python commands inside an activated virtualenv.

### Exercise 01

GraphX / GraphFrames: sum of `distance * multiplier` per node, and total cost of League of Legends items with Pregel.

Needs a JDK (local Spark), a Jupyter kernel for Python 3 and internet access (the notebooks run `pip install pyspark==3.5.0` and fetch `graphframes:graphframes:0.8.3-spark3.5-s_2.12` from Maven).

```bash
cd snippets/ex01_graphx/

# open in Jupyter or VS Code, then run all cells
#   distances.ipynb
#   lol-items.ipynb
```

See [`ex01_graphx/README.md`](ex01_graphx/README.md) for the challenges.

### Exercise 02

Higher-order array functions (`transform`, `filter`, `withField`) on a fraud transactions dataset.

Needs Spark (README badge says `3.3.1`) and the [Fraudulent Transactions Data](https://www.kaggle.com/datasets/chitwanmanchanda/fraudulent-transactions-data) CSV inside `ex02_high_order_functions/data/raw/`.

```bash
cd snippets/ex02_high_order_functions/
spark-submit main.py
```

### Exercise 03

Write a single fixed-width (positional) file from a CSV with Spark Shell.

Needs Spark (README badge says `3.0.x`) and the [Avocado Prices](https://www.kaggle.com/neuromusic/avocado-prices) CSV as `ex03_positional_file/data/avocado.csv`.  
`run.sh` runs `main.scala` in `spark-shell` and leaves the result in `data/AVOCADO.TXT`.

```bash
cd snippets/ex03_positional_file/
bash -x run.sh
```

### Exercise 04

Delta Lake liquid clustering: compare table size and files before and after `CLUSTER BY` on airline data.

Databricks notebook, not meant to run locally. It reads `/databricks-datasets/asa/airlines/` and writes a Delta table to a schema that is hard coded in the SQL cells, so change it to one you own first.

```bash
# import this notebook into a Databricks workspace and attach it to a cluster
snippets/ex04_liquid_clustering/lab-liquid-clustering.ipynb
```
