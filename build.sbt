name := "scala-worksheets-2"

version := "0.1"

scalaVersion := "2.12.8"

val circeVersion = "0.8.0"
val catsVersion = "1.5.0"
val sparkVersion = "2.4.0"
val slf4jVersion = "1.7.16"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-graphx" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.slf4j" % "slf4j-api" % slf4jVersion

).map(_.exclude("ch.qos.logback", "*"))


scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Xlint:missing-interpolator",
  "-Ywarn-unused-import",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-Ypartial-unification",
  "-language:_",
  "-encoding", "UTF-8"
)
