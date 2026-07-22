package br.avcaliani.kwikechain.utils

/** Argument Parsing Util.
  */
object ArgParse {

  /** Extract the pipeline name from the {@code -p | --pipeline} CLI argument.
    *
    * @param args
    *   Command-line arguments.
    * @return
    *   Pipeline name.
    */
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
