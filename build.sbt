name := "spark-app"
version := "26.07.00"

scalaVersion := "2.13.18"
javacOptions ++= Seq("-source", "21", "-target", "21")

libraryDependencies += "org.apache.spark" %% "spark-core" % "4.1.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "4.1.1"
libraryDependencies += "io.delta" %% "delta-spark" % "4.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % Test
