import functions.spark._
import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}
import org.apache.spark.sql.functions._
import org.json4s._
import org.json4s.jackson.Serialization.{read, write}
import scala.reflect.runtime.universe.TypeTag
import java.sql.Timestamp
import java.time.Instant

val spark: SparkSession = SparkSession
  .builder()
  .master("local[3]")
  .appName("test3")
  .config("spark.ui.showConsoleProgress", true)
  .getOrCreate()
import spark.implicits._
implicit val formats: Formats = DefaultFormats

val incomingStream = (List(
  InItem("A", 1, None, Timestamp.valueOf("2022-06-13 01:00:00")),
  InItem("B", 2, Some(2.3333333333), Timestamp.valueOf("2022-06-13 02:00:00")),
  InItem("C", 3, Some(33.0), Timestamp.valueOf("2022-06-13 03:00:00"))
).zipWithIndex
  .map { case (in, idx) =>
    (null, write(in), "incoming", 0, idx, Timestamp.from(Instant.now()))
  } ++ List(
  (null, null, "incoming", 0, 99, Timestamp.from(Instant.now())),
  (null, "{f{':ff", "incoming", 0, 100, Timestamp.from(Instant.now()))
))
  .toDF("key", "value", "topic", "partition", "offset", "timestamp")

// create
incomingStream.printSchema
incomingStream.show(false)
/*
root
 |-- key: void (nullable = true)
 |-- value: string (nullable = true)
 |-- topic: string (nullable = true)
 |-- partition: integer (nullable = false)
 |-- offset: integer (nullable = false)
 |-- timestamp: timestamp (nullable = true)

res0: Unit = ()
+----+----------------------------------------------------------------------------------------------------+--------+---------+------+-----------------------+
|key |value                                                                                               |topic   |partition|offset|timestamp              |
+----+----------------------------------------------------------------------------------------------------+--------+---------+------+-----------------------+
|null|{"RAW_NAME":"A","RAW_VALUE":1,"SOME_TIME":"2022-06-12T23:00:00Z"}                                   |incoming|0        |0     |2022-06-13 15:41:54.079|
|null|{"RAW_NAME":"B","RAW_VALUE":2,"OPTIONAL_RAW_DOUBLE":2.3333333333,"SOME_TIME":"2022-06-13T00:00:00Z"}|incoming|0        |1     |2022-06-13 15:41:54.081|
|null|{"RAW_NAME":"C","RAW_VALUE":3,"OPTIONAL_RAW_DOUBLE":33.0,"SOME_TIME":"2022-06-13T01:00:00Z"}        |incoming|0        |2     |2022-06-13 15:41:54.082|
+----+----------------------------------------------------------------------------------------------------+--------+---------+------+-----------------------+
 */


class Transformer[Out <: Product : TypeTag, In <: Convertable[Out]: TypeTag] {

  def transform(incoming:DataFrame):DataFrame =
    incoming
      .select(
        from_json('value, Encoders.product[In].schema).as("value")
      )

//      .as[CDFRecord[Out, In]]
//      .map(_.convert)
      .toDF
}

val transformedStream  = new Transformer[OutItem, InItem].transform(incomingStream)

transformedStream.printSchema
transformedStream.show(false)
/*
root
 |-- name: string (nullable = true)
 |-- int_value: integer (nullable = false)
 |-- decimal: decimal(38,18) (nullable = true)
 |-- timestamp: long (nullable = false)
 |-- ingested_at: long (nullable = false)
 |-- ingest_id: string (nullable = true)

res2: Unit = ()
+----+---------+---------------------+-------------+-------------+------------------------------------+
|name|int_value|decimal              |timestamp    |ingested_at  |ingest_id                           |
+----+---------+---------------------+-------------+-------------+------------------------------------+
|A   |1        |0E-18                |1655074800000|1655213536030|1229a835-58e2-4e55-b94e-7443cff5cee4|
|B   |2        |2.333333333300000000 |1655078400000|1655213536238|25faca42-b4f7-41c2-9936-30791c1c6139|
|C   |3        |33.000000000000000000|1655082000000|1655213536238|c9abc8b7-2a65-445e-9574-328d557f700e|
+----+---------+---------------------+-------------+-------------+------------------------------------+

 */