package br.avcaliani.scala.app.utils

/**
 * Simple Timer.
 */
object Timer extends Serializable {

  private var startedAt: Double = 0

  /**
   * Start timer.
   */
  def start(): Unit = startedAt = System.currentTimeMillis().toDouble

  /**
   * Return time elapsed in seconds.
   *
   * @return Time elapsed.
   */
  def stop(): Double = (System.currentTimeMillis().toDouble - startedAt) / 1000

}
