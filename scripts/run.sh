#!/bin/bash
#
# Script Name       : run.sh
# Author            : Anthony Vilarim Caliani
# GitHub            : https://github.com/avcaliani
# Date              : 2020.04.xx (migrated 2026-07)
# License           : MIT
#
# Description       : Generates the mock DonutCoin dataset (if missing) and
#                     runs "spark-app" through Spark Submit.
cd "$(dirname "$0")/.."

JAR_DIR="target/scala-*"
BRONZE_DIR="data/bronze/donu-transactions"

INFO="\033[0;34mINFO\033[00m:"
ERROR="\033[0;31mERROR\033[00m:"


# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=
#  FUNCTIONS
# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

is_installed() {
    if [ -z $(command -v $1) ]; then
      echo -e "${ERROR} '$1' not found, please check it first."
      exit 0
    fi
}


# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=
#  RUNNING...
# =~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=~=

echo "
   ___                   _
  / __| _ __  __ _  _ _ | |__
  \\__ \\| '_ \/ _\` || '_|| / /
  |___/| .__/\\__,_||_|  |_\\_\\
       |_|      APP v26.07.00
"

is_installed "java"
is_installed "spark-submit"
is_installed "sbt"

if [ ! -d "${BRONZE_DIR}" ] || [ -z "$(ls -A "${BRONZE_DIR}" 2>/dev/null)" ]; then
    echo -e "${INFO} No bronze data found at '${BRONZE_DIR}', generating mock DonutCoin transactions..."
    bash scripts/generate_donu_transactions.sh
fi

echo -e "${INFO} Building project..."
sbt clean compile package

jar_file=$(ls ${JAR_DIR}/*.jar)
echo -e "${INFO} JAR File -> '${jar_file}'"

echo -e "${INFO} Starting Spark..."
spark-submit \
    --master local \
    --packages "io.delta:delta-spark_2.13:4.2.0" \
    --conf "spark.sql.extensions=io.delta.sql.DeltaSparkSessionExtension" \
    --conf "spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog" \
    ${jar_file}

echo -e "${INFO} Process has been finished \\o/"
