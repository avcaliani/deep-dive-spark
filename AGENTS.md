## Who & Why

Python-first data engineer (PySpark, Airflow) using this repo as a Spark learning lab, in Scala and in PySpark.  
Prioritize clarity and teaching over cleverness.

When explaining Scala: assume strong PySpark intuition, little Scala syntax.
Anchor explanations to PySpark analogues.
Be terse.

## Repo Layout

Read [`README.md`](README.md) first, it has the file tree.
The pocs share one universe, explained in [`pocs/README.md`](pocs/README.md).
Every poc and snippet has its own README with how to run it, except `snippets/scripts` and `snippets/notebooks` (documented in `snippets/README.md`).

Stack: Java 21, Spark 4.1.1, Delta Lake 4.3.1. Keep versions aligned across pocs.

**NEVER run Python or PySpark commands outside the virtual env** (`python`, `pip`, `pytest`, `ruff`, `spark-submit`, `jupyter`).
Always use `uv run ...` from the folder that owns the `pyproject.toml` (for snippets, `snippets/` or any folder below it), or the project's container. This applies to every PySpark project here, whatever it is.

## Keep Docs Updated

During your work, if you notice incorrect or outdated information in Markdown files
or code comments and docs, alert the user and suggest the update.
