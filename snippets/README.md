<img src="../.docs/spark-logo.svg" align="right" height="64px"/>

# Snippets

Isolated Spark experiments, one folder each. They share nothing and can be run in any order.  
Datasets from Kaggle are not versioned, each experiment says where to put them.  
Run Python commands inside an activated virtualenv.

### GraphX

GraphX / GraphFrames: sum of `distance * multiplier` per node, and total cost of League of Legends items with Pregel.

Needs a JDK (local Spark), a Jupyter kernel for Python 3 and internet access (the notebooks run `pip install pyspark==3.5.0` and fetch `graphframes:graphframes:0.8.3-spark3.5-s_2.12` from Maven).

```bash
cd snippets/graphx/

# open in Jupyter or VS Code, then run all cells
#   distances.ipynb
#   lol-items.ipynb
```

See [`graphx/README.md`](graphx/README.md) for the challenges.

### High Order Functions

Higher-order array functions (`transform`, `filter`, `withField`) on a fraud transactions dataset.

Needs Spark (README badge says `3.3.1`) and the [Fraudulent Transactions Data](https://www.kaggle.com/datasets/chitwanmanchanda/fraudulent-transactions-data) CSV inside `high-order-functions/data/raw/`.

```bash
cd snippets/high-order-functions/
spark-submit main.py
```

### Positional File

Write a single fixed-width (positional) file from a CSV with Spark Shell.

Needs Spark (README badge says `3.0.x`) and the [Avocado Prices](https://www.kaggle.com/neuromusic/avocado-prices) CSV as `positional-file/data/avocado.csv`.  
`run.sh` runs `main.scala` in `spark-shell` and leaves the result in `data/AVOCADO.TXT`.

```bash
cd snippets/positional-file/
bash -x run.sh
```

### Liquid Clustering

Delta Lake liquid clustering: compare table size and files before and after `CLUSTER BY` on airline data.

Databricks notebook, not meant to run locally. It reads `/databricks-datasets/asa/airlines/` and writes a Delta table to a schema that is hard coded in the SQL cells, so change it to one you own first.

```bash
# import this notebook into a Databricks workspace and attach it to a cluster
snippets/liquid-clustering/lab-liquid-clustering.ipynb
```
