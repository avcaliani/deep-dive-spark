package br.avcaliani.scala.app

import br.avcaliani.scala.app.utils.{FSUtil, Timer}
import br.avcaliani.scala.app.validator.TransactionValidator
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

// TODO: Setup argument parsing to receive the pipeline name -p | --pipeline
// In Main, parse the arguments and trigger the pipeline according to the pipeline name
//
// Also, create a Pipeline trait that forces the run(): Unit method implementation

object Main extends App with Props {

  val log = LoggerFactory.getLogger(getClass)
  val APP_NAME        = getProperty("app.name")
  val DATA_BRONZE     = getProperty("data.bronze")
  val DATA_SILVER_OK  = getProperty("data.silver.success")
  val DATA_SILVER_ERR = getProperty("data.silver.error")
  val DATA_SILVER_DBG = getProperty("data.silver.debug")
  val CLUSTER_BY      = Seq("from_user", "to_user")

  val sc = new SparkConf().setMaster("local[4]").setAppName(APP_NAME)
  val ss = SparkSession.builder().config(sc).getOrCreate()
  val fs = new FSUtil(ss, FileSystem.get(ss.sparkContext.hadoopConfiguration))


  Timer.start()
  val df: DataFrame = fs.read(DATA_BRONZE)
  log.debug(s"Task::Read -> ${ Timer.stop() }s")


  Timer.start()
  val dfValidated = new TransactionValidator(ss).validate(df)
  val dropColumns: Array[String] = dfValidated.columns.filter(_.endsWith("_valid"))
  val dfOk  = dfValidated.filter(col("is_valid") === true)
  val dfErr = dfValidated.filter(col("is_valid") === false)
  log.debug(s"Task::Validate -> ${ Timer.stop() }s")


  Timer.start()
  fs.writeDelta(DATA_SILVER_OK, dfOk.drop(dropColumns: _*), CLUSTER_BY)
  fs.writeDelta(DATA_SILVER_ERR, dfErr.drop(dropColumns: _*), CLUSTER_BY)
  fs.writeDelta(DATA_SILVER_DBG, dfValidated, CLUSTER_BY)
  log.debug(s"Task::Write -> ${ Timer.stop() }s")

}
