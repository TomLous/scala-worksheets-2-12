name := "scala-worksheets-2"

version := "0.1"

scalaVersion := "2.12.16"

val circeVersion = "0.14.1"
val catsVersion = "2.7.0"
val sparkVersion = "3.2.1"
val slf4jVersion = "1.7.30"

resolvers ++= Seq(
  "Splunk Releases" at "https://splunk.jfrog.io/splunk/ext-releases-local"
)

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
  "com.github.vickumar1981" %% "stringdistance" % "1.2.7",
  "org.json4s" %% "json4s-jackson" % "3.6.9",
  "com.splunk.logging" % "splunk-library-javalogging" % "1.11.5" % Runtime
).map(_.exclude("ch.qos.logback", "*"))

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Xlint:missing-interpolator",
  "-Ypartial-unification",
  "-language:_",
  "-encoding",
  "UTF-8"
)
