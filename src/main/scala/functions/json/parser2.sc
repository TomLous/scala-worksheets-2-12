import cats.implicits._
import io.circe.optics.JsonPath._
import io.circe.parser._
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

import scala.io.Source

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

val _header = root.header.each.string
val _values = root.values.each.arr


val rawJsonStr = Source.fromResource("raw_json2.json").mkString


(for {
  json <- parse(rawJsonStr) // parse json
  rows <- Right(_values.getAll(json)
    .map(record => Row.fromSeq(record.map(_.asString.orNull)))
  )
  schema <- Right( // Create schema
    StructType(
      _header.getAll(json).map(StructField(_, StringType, true))
    )
  )
} yield (schema,rows))  match {
  case Left(e) => println(s"Parse error: ${e.getMessage}")
  case Right((schema,rows)) =>
    spark.sqlContext.createDataFrame(
      spark.sparkContext.parallelize(rows), schema)
      .show(1000, false)
}


/**
 * +-------------------------------------------------+----------+
 * |Product                                          |GSAP_MATNR|
 * +-------------------------------------------------+----------+
 * |https://rdf.shell.com/product/ago_vpower_at      |400002384 |
 * |https://rdf.shell.com/product/ago_b7_de          |400001684 |
 * |https://rdf.shell.com/product/ago_b7_at          |400002489 |
 * |https://rdf.shell.com/product/ago_b7_at          |400002477 |
 * |https://rdf.shell.com/product/ago_b7_at          |400002498 |
 * |https://rdf.shell.com/product/ago_b7_de          |400008681 |
 * |https://rdf.shell.com/product/ago_vpower_de      |400002124 |
 * |https://rdf.shell.com/product/ago_b7_belux       |400002859 |
 * |https://rdf.shell.com/product/ago_b0_de          |400001681 |
 * |https://rdf.shell.com/product/ago_vpower_fr      |400002912 |
 * |https://rdf.shell.com/product/ago_b7_belux       |400002858 |
 * |https://rdf.shell.com/product/ago_b7_de          |400001682 |
 * |https://rdf.shell.com/product/ago_b7_de          |400002130 |
 * |https://rdf.shell.com/product/ago_vpower_de      |400001968 |
 */





