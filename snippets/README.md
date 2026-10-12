<img src="../.docs/spark-logo.svg" align="right" height="64px"/>

# Snippets

Isolated Spark experiments, one folder each. They share nothing and can be run in any order.

```text
.
├── graphx/                 GraphFrames: distances per node + LoL item cost with Pregel (PySpark 4.1.1, uv)
├── high-order-functions/   transform / filter / withField on fraud transactions (PySpark 4.1.1, uv)
├── positional-file/        fixed-width file writer from a CSV (Scala, spark-shell, Spark 3.0.x)
└── liquid-clustering/      Delta CLUSTER BY, size and files before vs after (Databricks notebook)
```

Each folder has its own README with how to run it.  
Python experiments are uv projects: run them with `uv run` from their folder, never outside the virtual env.
