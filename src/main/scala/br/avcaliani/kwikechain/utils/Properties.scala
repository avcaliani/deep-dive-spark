package br.avcaliani.kwikechain.utils

import java.util.{Properties => JavaProperties}

import scala.util.Try

/** Properties Trait.
  */
trait Properties extends Serializable {

  private val properties: JavaProperties = {
    val p = new JavaProperties()
    p.load(getClass.getResourceAsStream("/app.properties"))
    p
  }

  /** Return a property value based on property {@code key} . It throws an {@link RuntimeException} if property key
    * doesn't exist.
    *
    * @param key
    *   Property Key.
    * @return
    *   Property Value.
    */
  def getProperty(key: String): String = {
    Try(properties.getProperty(key)).getOrElse(
      throw new RuntimeException(s"'${key}' key not found in properties file!")
    )
  }

}
