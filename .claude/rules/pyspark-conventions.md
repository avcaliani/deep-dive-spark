---
paths:
  - "pocs/kwik-e-mart/**/*.py"
  - "snippets/**/*.py"
---

# PySpark Conventions (pocs/kwik-e-mart and snippets)

## NEVER run commands outside the virtual env

Every Python or PySpark command runs through uv (or the poc container). Never use bare `python`,
`python3`, `pip`, `pytest`, `ruff`, `spark-submit`, `pyspark` or `jupyter`.

```bash
# right
cd pocs/kwik-e-mart && uv run pytest
cd snippets/graphx && uv run python distances.py
cd snippets/high-order-functions && uv run spark-submit main.py

# wrong
python distances.py
pip install pyspark
spark-submit main.py
```

- Each snippet with Python code is its own uv project (`pyproject.toml`, `uv.lock`, `.python-version`).
  Add dependencies with `uv add`, pinned to exact versions, never with `pip install`.
- `snippets/positional-file` (Scala) and `snippets/liquid-clustering` (Databricks) are not Python.
- Spark needs JDK 21 on the machine. Without it, use the poc container instead of installing anything globally.

## Pipelines (pocs/kwik-e-mart)

- A pipeline subclasses `Pipeline` (`pipelines/base.py`) and implements `run(self) -> None`.
- Register it in `pipelines/__init__.py` (`PIPELINES`), the key is the CLI name (`-p <name>`).
- Keep the transform logic in `@classmethod`s (`rename_cols`, `parse_cols`, `data_quality`) chained by `process`,
  so tests call them without a full job.
- Paths come from `DATA_PATH` (env), never hard-coded.

## DataFrame code

- `from pyspark.sql import functions as f`, then `f.col(...)`.
- One small method or function per concern, each takes and returns a DataFrame.
- Cast and parse explicitly (`try_to_date`, `cast('long')`), never rely on inference for silver.
- Log through `utils.log` in the poc, not `print`. Snippets may `print` or `show()` their results.

## Style

- Single quotes, line length 120, ruff rules `E, F, I, UP` (see `pocs/kwik-e-mart/pyproject.toml`).
- Tests in `tests/` with the shared Spark fixture in `conftest.py`; `src` is on `pythonpath`.
- Snippet scripts: a module docstring with the problem, the run command and any diagram in `.docs/`;
  small typed functions (`spark_session`, `build_*`, `calculate_*`, `main`); self-checking with an `assert`
  on the expected result when there is one.

## GraphFrames notes (snippets/graphx)

- Python package `graphframes-py` plus the jar `io.graphframes:graphframes-spark4_2.13` through `spark.jars.packages`.
- `graphframes-py` does not declare `numpy` and `typing-extensions`, so the project pins them itself.
- Since GraphFrames 0.12, Pregel messages only see the edge columns declared with `required_edge_columns`.
