package br.avcaliani.kwikechain.utils

import java.util.{Properties => JavaProperties}

import scala.util.Try

trait Properties extends Serializable {

  private val properties: JavaProperties = {
    val props = new JavaProperties()
    props.load(getClass.getResourceAsStream("/app.properties"))
    props
  }

  def getProperty(key: String): String = {
    Try(properties.getProperty(key)).getOrElse(
      throw new RuntimeException(s"'${key}' key not found in properties file!")
    )
  }

}
