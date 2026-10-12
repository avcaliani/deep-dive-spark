# Spark Concepts Used Here

## Bronze to silver

Both pocs read raw files from `data/bronze/` and write Delta tables to `data/silver/`.
Bronze is never modified. Silver is typed, validated and safe to query.
Delta paths must be absolute (a leading `./` breaks path-based table addressing).

## Lazy evaluation and `cache`

Transformations (`select`, `filter`, `join`) only build a plan. Actions (`count`, `show`, `write`) run it.
If one DataFrame feeds several actions, `cache()` it or Spark recomputes it each time
(`DailyTransactionsPipeline` caches the flagged rows because it writes them three times).

## Validate and route

Add boolean `*_valid` columns plus an `is_valid` flag, then split:
- `success`: valid rows, flag columns dropped
- `error`: invalid rows, flag columns dropped
- `debug`: everything, flags kept (to see why a row failed)

PySpark analogue: `df.withColumn('is_valid', cond)` then two `filter` calls.

## Broadcast join

Small lookup tables (e.g. known users) go through `broadcast(df)` so each executor gets a copy
and the big side is not shuffled.

## Liquid clustering

Delta tables are written with `CLUSTER BY` columns (`from_user, to_user`; `branch, date`) instead of
`partitionBy`. Pick columns that are filtered or joined on often. Cluster keys can change without a rewrite.

## Pipeline registry

Each poc has a name-to-pipeline map (`pipelines/package.scala`, `pipelines/__init__.py`).
`-p <name>` on the command line picks the pipeline. Add new pipelines there.

## Local runs

Pocs run Spark in `local` mode inside the container. No cluster, no S3. Good for learning,
not a performance benchmark.
