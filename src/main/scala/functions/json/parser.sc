import io.circe._
import io.circe.parser._
import io.circe.optics.JsonPath._
import cats.implicits._

import scala.io.Source
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val _header = root.header.each.string
val _values = root.values.each.arr


val rawJsonStr = Source.fromResource("raw_json.json").mkString


(for {
  json <- parse(rawJsonStr) // parse json
  headers <- Right(_header.getAll(json)) // read header object
  values <- Right(_values.getAll(json)) // read values object
  firstValues <- Right( // first value is not mapped, but string to first header item (Product) => So create List[(String -> String)]
    values
      .map(_.head.asString.getOrElse(""))
      .map(headers.head -> _)
  )
  otherValueMappings <- values // Other values are mapped, order might not be guaranteed => List[Map[String, String]]
    .flatMap(_.tail)
    .map(_.as[Map[String, String]])
    .sequence
  recordsMap <- Right((otherValueMappings zip firstValues).map { // Combine the first value with the other mapping
    case (map, item) => (map + item)
  })
  rows <- // For each line with mapping => create Spark Row
      Right(
        recordsMap
          .map(record =>
            headers.map(
              record.get(_).orNull
            )
          )
          .map(Row.fromSeq) // Turn into a row
      )
  schema <- Right( // Create schema
    StructType(
      headers.map(StructField(_, StringType, true))
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





