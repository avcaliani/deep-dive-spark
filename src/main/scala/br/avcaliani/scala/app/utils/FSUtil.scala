package br.avcaliani.scala.app.utils

import io.delta.tables.DeltaTable
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

/**
 * File System Util.
 *
 * @param ss {@link SparkSession}.
 * @param fs {@link FileSystem}.
 */
class FSUtil(ss: SparkSession, fs: FileSystem) extends Serializable {

  val log = LoggerFactory.getLogger(getClass)

  /**
   * Read file data and put it into a {@link DataFrame} structure.
   *
   * @param path Entry File Path.
   * @param delimiter Optional Delimiter.
   * @return {@link DataFrame}.
   */
  def read(path: String, delimiter: String = ","): DataFrame = {
    log.info(s"Reading file '$path'...")
    val df: DataFrame = ss
      .read
      .option("header", "true")
      .option("delimiter", delimiter)
      .csv(path)
    log.info(s"OK")
    df
  }

  /**
   * Write {@link DataFrame} into a Delta table clustered on {@code clusterBy}.
   *
   * Creates the table (with its Liquid Clustering spec) only on the very
   * first write to {@code path}; every write after that clears existing
   * rows via {@code DELETE} and appends instead of recreating the table --
   * calling {@code createOrReplace} unconditionally on every run would work
   * too, but silently resets the table definition (and its clustering
   * metadata) each time, which we don't want on repeated `scripts/run.sh`
   * invocations.
   *
   * @param path Output Table Path.
   * @param df {@link DataFrame}.
   * @param clusterBy Column names to cluster the Delta table on.
   */
  def writeDelta(path: String, df: DataFrame, clusterBy: Seq[String]): Unit = {
    require(clusterBy.nonEmpty, "clusterBy must contain at least one column")

    // DeltaTable.createOrReplace(...).location(path) builds SQL internally
    // (delta.`{path}`), and a relative path starting with ./ breaks that
    // parser -- normalize to absolute first.
    val absolutePath = new java.io.File(path).getAbsolutePath

    if (DeltaTable.isDeltaTable(ss, absolutePath)) {
      DeltaTable.forPath(ss, absolutePath).delete()
    } else {
      DeltaTable.createOrReplace(ss)
        .addColumns(df.schema)
        .clusterBy(clusterBy: _*)
        .location(absolutePath)
        .execute()
    }

    df.write.format("delta").mode("append").save(absolutePath)
    log.info(s"Delta table written at '$absolutePath'")
  }

}
