---
paths:
  - "**/*.scala"
---

# Scala Conventions (pocs/kwik-e-chain)

## Naming

| Thing            | Convention             | Example                     |
|------------------|------------------------|-----------------------------|
| Packages         | lowercase              | `br.avcaliani.kwikechain`   |
| Classes / traits | `PascalCase`           | `DailyTransactionsPipeline` |
| Methods / vals   | `camelCase`            | `withKnownUserFlag`         |
| Constants        | `UPPER_SNAKE` private  | `CLUSTER_BY`                |
| DataFrame columns| `snake_case` strings   | `"amount_cents"`            |

## Pipelines

- A pipeline is a `case class X(spark: SparkSession) extends Pipeline` with `run(): Unit`.
- Register it in `pipelines/package.scala` (`registry`), the key is the CLI name (`-p daily-transactions`).
- Read paths and settings from `app.properties` through the `Properties` trait, no hard-coded paths.

## DataFrame code

- Keep steps small and compose them with `.transform(...)`.
- One `Column` per rule (`isPositiveAmount`, `isFeeValid`), named like a predicate.
- Curried helpers `(args)(df: DataFrame): DataFrame` so they plug into `.transform`.
- Use `import spark.implicits._` and `$"col"` for columns.
- Log with slf4j (`LoggerFactory`), not `println`. Use `log.info` for counts, `log.debug` for task markers.

## Tests and format

- Specs live in `src/test/scala`, the shared Spark session is `utils/SparkSpec.scala`.
- Test pure `validate`-style functions on small in-memory DataFrames, not the full job.
- Format with `sbt scalafmtAll` (config in `.scalafmt.conf`).
