package br.avcaliani.kwikechain

import org.apache.spark.sql.SparkSession

/** Pipeline Registry.
  */
package object pipelines {

  val registry: Map[String, SparkSession => Pipeline] = Map(
    "daily-transactions" -> (spark => new DailyTransactionsPipeline(spark))
  )
}
