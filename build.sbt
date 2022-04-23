name := "scala-worksheets-2"

version := "0.1"

scalaVersion := "2.12.8"

val circeVersion = "0.12.0"
val catsVersion = "1.5.0"
val sparkVersion = "2.4.4"
val slf4jVersion = "1.7.16"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-graphx" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-optics" % circeVersion,
  "com.github.vickumar1981" %% "stringdistance" % "1.1.1",

).map(_.exclude("ch.qos.logback", "*"))

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Xlint:missing-interpolator",
  "-Ypartial-unification",
  "-language:_",
  "-encoding", "UTF-8"
)
