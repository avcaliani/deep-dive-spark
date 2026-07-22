package br.avcaliani.kwikechain.utils

object ArgParse {

  def pipelineName(args: Array[String]): String =
    args
      .sliding(2)
      .collectFirst {
        case Array("-p", name)         => name
        case Array("--pipeline", name) => name
      }
      .getOrElse(
        throw new IllegalArgumentException("Missing required argument -p | --pipeline.")
      )

}
