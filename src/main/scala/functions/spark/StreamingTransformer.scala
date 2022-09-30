package functions.spark
import functions.spark._
import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}
import org.apache.spark.sql.functions._
import org.json4s._
import org.json4s.jackson.Serialization.{read, write}
import scala.reflect.runtime.universe.TypeTag
import java.sql.Timestamp
import java.time.Instant

class StreamingTransformer[Out <: Product : TypeTag, In <: Convertable[Out]: TypeTag]{
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

  val transformedStream = incomingStream
    .select(
      from_json('value, Encoders.product[In].schema).as("value")
    )
    .as[CDFRecord[Out, In]]
    .map(_.convert)
    .toDF


    transformedStream.printSchema
    transformedStream.show(false)


}
