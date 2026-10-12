name := "kwikechain"
version := "26.07.00"

scalaVersion := "2.13.18"
javacOptions ++= Seq("-source", "21", "-target", "21")


libraryDependencies += "org.apache.spark" %% "spark-core" % "4.1.1" % "provided,test"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "4.1.1" % "provided,test"
libraryDependencies += ("io.delta" %% "delta-spark" % "4.3.1")
  .excludeAll(
    ExclusionRule("org.slf4j"),
    ExclusionRule("org.apache.logging.log4j")
  )
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % Test

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "services", _*) => MergeStrategy.filterDistinctLines
  case PathList("META-INF", _*)             => MergeStrategy.discard
  case "module-info.class"                  => MergeStrategy.discard
  case _                                     => MergeStrategy.first
}
