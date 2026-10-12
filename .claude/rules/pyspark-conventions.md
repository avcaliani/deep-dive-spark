---
paths:
  - "**/*.py"
---

# PySpark Conventions

Apply to any PySpark code in this repo, whatever the project.

## NEVER run commands outside the virtual env

Every Python or PySpark command runs through uv, from the folder that owns the `pyproject.toml`,
or inside the project's container. Never use bare `python`, `python3`, `pip`, `pytest`, `ruff`,
`spark-submit`, `pyspark` or `jupyter`.

```bash
# right
cd <project> && uv run pytest
cd <project> && uv run python main.py
cd <project> && uv run spark-submit main.py

# wrong
python main.py
pip install pyspark
spark-submit main.py
```

- Every Python project is its own uv project (`pyproject.toml`, `uv.lock`, `.python-version`). Start new ones with `uv init`.
- Add dependencies with `uv add`, pinned to exact versions. Never `pip install`.
- Spark needs JDK 21 on the machine. Without it, use the project's container instead of installing anything globally.
- Keep `pyspark` and `delta-spark` aligned with the repo stack (see `AGENTS.md`).
- Libraries can under-declare their dependencies. If an import fails in the venv, `uv add` the missing package
  instead of installing it anywhere else.

## Default project layout (pipelines)

New PySpark pipeline projects follow this shape. Small throwaway scripts can skip the registry and the base class.

```text
src/                    on pytest's `pythonpath`
├── main.py             builds the SparkSession, parses `-p/--pipeline`, runs it, stops the session
├── pipelines/
│   ├── base.py         `Pipeline(ABC)`: holds `spark`, abstract `run(self) -> None`
│   ├── __init__.py     `PIPELINES: dict[str, type[Pipeline]]`, kebab-case key = CLI name
│   └── <domain>/       one module per pipeline
└── utils/              `file.py` (read/write helpers), `log.py` (logger)
tests/                  `conftest.py` with the shared Spark fixture
scripts/                `run.sh` (spark-submit) and the mock data generator
resources/              `log4j.properties`
```

- Transform logic lives in `@classmethod`s that take and return a DataFrame (`rename_cols`, `parse_cols`, `data_quality`),
  chained by a `process` method, so tests call them without running a whole job.
- The data root comes from the `DATA_PATH` env var and datasets follow `bronze/<domain>/<dataset>`
  to `silver/<domain>/<dataset>`.
- Log through `utils.log`, not `print`. `print` and `show()` are fine in throwaway scripts.

## DataFrame code

- `from pyspark.sql import functions as f`, then `f.col(...)`.
- One small function per concern, taking and returning a DataFrame, so each can be tested alone and chained with `.transform`.
- Cast and parse explicitly (`try_to_date`, `cast('long')`), never rely on schema inference for curated data.
- Paths and settings come from arguments or environment variables, never hard-coded.
- Build the `SparkSession` in one `spark_session()` function and stop it at the end.

## Delta writes

- Normalize the path to absolute first: a relative path starting with `./` breaks Delta's path-based table addressing.
- New table: `DeltaTable.createOrReplace(spark).addColumns(df.schema).clusterBy(...).location(path).execute()`,
  then append.
- Existing table: `DeltaTable.forPath(spark, path).delete()` then append. Do not use `mode('overwrite')`, it rewrites the
  table definition and silently drops the clustering metadata.

## Style and tests

- Type hints on every function, small functions, `main()` behind `if __name__ == '__main__':`.
- Single quotes, line length 120, ruff rules `E, F, I, UP` in the project's `pyproject.toml`. Check with `uv run ruff check .`.
- Tests use a session-scoped local `SparkSession` fixture (`conftest.py`, `master('local[2]')`) and
  `pyspark.testing.assertDataFrameEqual`. Test the transform functions on small in-memory DataFrames, not the full job.
