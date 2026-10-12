package br.avcaliani.kwikechain

import org.apache.spark.sql.SparkSession

package object pipelines {

  val registry: Map[String, SparkSession => Pipeline] = Map(
    "daily-transactions" -> (spark => DailyTransactionsPipeline(spark))
  )
}
