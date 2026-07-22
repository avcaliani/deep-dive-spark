package br.avcaliani.kwikechain.pipelines

/** Pipeline Trait */
trait Pipeline extends Serializable {

  /** Run the pipeline */
  def run(): Unit

}
