---
paths:
  - "pocs/kwik-e-mart/**/*.py"
---

# Kwik-E-Mart Pipeline Conventions

Specific to `pocs/kwik-e-mart`. General PySpark rules live in `pyspark-conventions.md`.

- A pipeline subclasses `Pipeline` (`pipelines/base.py`) and implements `run(self) -> None`.
- Register it in `pipelines/__init__.py` (`PIPELINES`), the key is the CLI name (`-p <name>`).
- Keep the transform logic in `@classmethod`s (`rename_cols`, `parse_cols`, `data_quality`) chained by `process`,
  so tests call them without a full job.
- Paths come from `DATA_PATH` (env), never hard-coded.
- Log through `utils.log`, not `print`.
- Style: single quotes, line length 120, ruff rules `E, F, I, UP` (see `pyproject.toml`).
- Tests in `tests/` with the shared Spark fixture in `conftest.py`; `src` is on `pythonpath`.
