---
paths:
  - "pocs/the-paper-trail/**/*.py"
---

# PySpark Conventions (pocs/the-paper-trail)

## Pipelines

- A pipeline subclasses `Pipeline` (`pipelines/base.py`) and implements `run(self) -> None`.
- Register it in `pipelines/__init__.py` (`PIPELINES`), the key is the CLI name (`-p <name>`).
- Keep the transform logic in `@classmethod`s (`rename_cols`, `parse_cols`, `data_quality`) chained by `process`,
  so tests call them without a full job.
- Paths come from `DATA_PATH` (env), never hard-coded.

## DataFrame code

- `from pyspark.sql import functions as f`, then `f.col(...)`.
- One small method per concern, each takes and returns a DataFrame.
- Cast and parse explicitly (`try_to_date`, `cast('long')`), never rely on inference for silver.
- Log through `utils.log`, not `print`.

## Style

- Single quotes, line length 120, ruff rules `E, F, I, UP` (see `pyproject.toml`).
- Run everything through uv: `uv run pytest`, `uv run ruff check .`.
- Tests in `tests/` with the shared Spark fixture in `conftest.py`; `src` is on `pythonpath`.
