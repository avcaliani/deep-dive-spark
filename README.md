# ⭐️ Spark App
By Anthony Vilarim Caliani

![License](https://img.shields.io/github/license/avcaliani/deep-dive-spark?logo=apache&color=lightseagreen)
![Temurin](https://img.shields.io/badge/Temurin-21-FF7800?logo=eclipseadoptium&logoColor=white)
![Scala](https://img.shields.io/badge/Scala-2.13.18-DC322F?logo=scala&logoColor=white)
![sbt](https://img.shields.io/badge/sbt-1.12.1-1B5162)
![Spark](https://img.shields.io/badge/Apache--Spark-4.1.1-E25A1C?logo=apachespark&logoColor=white)
![Delta Lake](https://img.shields.io/badge/Delta_Lake-4.2.0-00ADD8)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

This is my Apache Spark project.  
Here you will find some stuff that I've done while learning about working with Spark and Scala.

The transaction ledger is a locally-generated mock "DonutCoin (DONU)" dataset, themed on The Simpsons, validated against the same cast/currency reference data used by the `kafka-in-docker` project.

> ℹ️ This is a reference scaffold you clone from to start new Spark/Scala projects, not a maintained showcase.

## Quick Start

```sh
# Download the known-users reference data (kafka-in-docker's Simpsons cast list)
mkdir -p data/bronze
curl -o data/bronze/users.csv \
  https://raw.githubusercontent.com/avcaliani/kafka-in-docker/refs/heads/main/scripts/users.csv

# Build the image
docker compose build

# Start the container
docker compose up -d

# Run the job (generates the mock dataset on first run, then builds & runs)
docker compose exec spark scripts/run.sh

# Run the tests
docker compose exec spark sbt test

# When you're done
docker compose down
```

## Formatting

Code formatting (`scalafmt`) runs locally, not inside the container -- requires JDK 21 and sbt installed on your machine:

```sh
sbt scalafmtAll
```

## Related Links
- [Apache Spark: Official Website](https://spark.apache.org)
- [Delta Lake: Official Website](https://delta.io)
- [SBT Commands](https://alvinalexander.com/scala/sbt-how-to-compile-run-package-scala-project)

PS.: This is an unofficial fan project for learning Spark/Scala, not affiliated with or endorsed by Fox/Disney.
