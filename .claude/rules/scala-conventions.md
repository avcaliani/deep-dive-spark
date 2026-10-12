---
paths:
  - "**/*.scala"
---

# Scala Conventions

Apply to any Scala code in this repo, whatever the project.

## Tooling

- Scala 2.13 with sbt. Run sbt from the folder that owns `build.sbt`, or inside the project's container.
- Spark needs JDK 21 on the machine. Without it, use the project's container instead of installing anything globally.
- Keep Spark and Delta aligned with the repo stack (see `AGENTS.md`).
- Build a fat jar with `sbt clean assembly` and run it through `spark-submit`.

## Default project layout (pipelines)

New Scala pipeline projects follow this shape.

```text
build.sbt, project/{plugins.sbt,build.properties}, .scalafmt.conf
src/main/resources/     app.properties, log4j2.properties
src/main/scala/<package>/
├── Main.scala          App: reads `-p/--pipeline`, looks it up in the registry, builds the SparkSession, runs it
├── pipelines/
│   ├── Pipeline.scala  trait Pipeline extends Serializable { def run(): Unit }
│   ├── package.scala   registry: Map[String, SparkSession => Pipeline], kebab-case key = CLI name
│   └── <Name>Pipeline.scala
└── utils/              ArgParse, Properties (reads app.properties), FSUtil (read CSV, write Delta)
src/test/scala/<package>/  specs, plus utils/SparkSpec.scala
```

- A pipeline is a `case class <Name>Pipeline(spark: SparkSession) extends Pipeline with Properties`.
- Read paths and settings from `app.properties` through the `Properties` trait, never hard-code them.
  Datasets follow `data/bronze` to `data/silver`.

## Naming

| Thing             | Convention             | Example                |
|-------------------|------------------------|------------------------|
| Packages          | lowercase              | `br.avcaliani.myapp`   |
| Classes / traits  | `PascalCase`           | `DailyOrdersPipeline`  |
| Methods / vals    | `camelCase`            | `withValidFlag`        |
| Constants         | `UPPER_SNAKE`, private | `CLUSTER_BY`           |
| DataFrame columns | `snake_case` strings   | `"order_id"`           |

## DataFrame code

- Keep steps small and compose them with `.transform(...)`.
- One `Column` per rule (`isPositiveAmount`), named like a predicate.
- Curried helpers `(args)(df: DataFrame): DataFrame` so they plug into `.transform`.
- Use `import spark.implicits._` and `$"col"` for columns.
- Log with slf4j (`LoggerFactory`), not `println`. Use `log.info` for counts, `log.debug` for task markers.

## Delta writes

- Normalize the path to absolute first.
- Require at least one `clusterBy` column.
- New table: `DeltaTable.createOrReplace(spark).addColumns(df.schema).clusterBy(...).location(path).execute()`, then append.
- Existing table: `DeltaTable.forPath(spark, path).delete()` then append. Do not use `mode("overwrite")`, it rewrites the
  table definition and silently drops the clustering metadata.

## Tests and format

- Specs extend `SparkSpec` (`AnyFlatSpec` with `Matchers`): a shared local `SparkSession`, stopped in `afterAll`,
  and an order-independent `assertDataFrameEquals`, since joins and broadcasts do not keep row order.
- Test pure functions (a `validate`-style step) on small in-memory DataFrames, not the full job.
- Format with `sbt scalafmtAll`: scalafmt 3.8.3, `scala213` dialect, `maxColumn = 120`, `align.preset = more`.
