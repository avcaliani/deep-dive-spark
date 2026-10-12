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

## DataFrame code

- `from pyspark.sql import functions as f`, then `f.col(...)`.
- One small function per concern, taking and returning a DataFrame, so each can be tested alone and chained with `.transform`.
- Cast and parse explicitly (`try_to_date`, `cast('long')`), never rely on schema inference for curated data.
- Paths and settings come from arguments or environment variables, never hard-coded.
- Build the `SparkSession` in one `spark_session()` function and stop it at the end.

## Style and tests

- Type hints on every function, small functions, `main()` behind `if __name__ == '__main__':`.
- Follow the ruff config in the project's `pyproject.toml` and check with `uv run ruff check .`.
- Tests use a session-scoped local `SparkSession` fixture (`conftest.py`) and `pyspark.testing.assertDataFrameEqual`.
  Test the transform functions on small in-memory DataFrames, not the full job.
- Log with the project's logger when it has one. `print` and `show()` are fine in throwaway scripts.
