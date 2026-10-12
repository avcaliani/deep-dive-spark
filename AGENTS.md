## Who & Why

Python-first data engineer (PySpark, Airflow) using this repo as a Spark learning lab, in Scala and in PySpark.  
Prioritize clarity and teaching over cleverness.

When explaining Scala: assume strong PySpark intuition, little Scala syntax.
Anchor explanations to PySpark analogues.
Be terse.

## Repo Layout

Read [`README.md`](README.md) first, it has the file tree.
The pocs share one universe, explained in [`pocs/README.md`](pocs/README.md).
Every poc and snippet has its own README with how to run it.

Stack: Java 21, Spark 4.1.1, Delta Lake 4.3.1. Keep versions aligned across pocs.

## How to run

**pocs/kwik-e-chain** (from `pocs/kwik-e-chain`, see its README)
 - docker: `docker compose build && docker compose up -d`
 - data: download `data/bronze/users.csv` (README), then `docker compose exec spark python3 scripts/generate_donu_transactions.py`
 - run: `docker compose exec spark scripts/run.sh`
 - test: `docker compose exec spark sbt test` (or `sbt test` locally with JDK 21 + sbt)
 - format: `sbt scalafmtAll`

**pocs/kwik-e-mart** (from `pocs/kwik-e-mart`, see its README)
 - docker: `docker compose build && docker compose up -d`
 - data: `docker compose exec app scripts/generate_sales_data.py`
 - run: `docker compose exec app scripts/run.sh`
 - test: `uv run pytest`
 - lint: `uv run ruff check .`

**snippets** (isolated experiments) are run one by one, see `snippets/README.md` and each folder's README (Kaggle datasets are not versioned).
 - `graphx`, `high-order-functions`: each is its own uv project, run `uv run ...` from the folder
 - `positional-file` (Scala `spark-shell`) and `liquid-clustering` (Databricks notebook) are not Python

**NEVER run Python or PySpark commands outside the virtual env** (`python`, `pip`, `pytest`, `ruff`, `spark-submit`, `jupyter`).
Always use `uv run ...` from the folder that owns the `pyproject.toml`, or the project's container. This applies to every PySpark project here, whatever it is.

## Keep Docs Updated

During your work, if you notice incorrect or outdated information in Markdown files
or code comments and docs, alert the user and suggest the update.
