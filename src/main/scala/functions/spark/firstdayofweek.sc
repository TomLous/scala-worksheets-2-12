import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}
import scala.reflect.runtime.universe._

val spark = SparkSession.builder()
  .master("local[2]")
  .getOrCreate()

import spark.implicits._

import org.apache.spark.sql.functions._

val df1 = Seq(
  (1, "2020-05-12 10:23:45", 5000),
  (2, "2020-11-11 12:12:12", 2000),
  (3, "2020-07-02 12:12:12", 2000),
  (4, "2020-06-29 12:12:12", 2000),
  (5, "2020-07-05 12:12:12", 2000),
  (6, "2020-07-06 12:12:12", 2000),
).toDF("id", "DateTime", "miliseconds")

df1
  .withColumn("FirstDayOfWeek",
     dayofweek('DateTime))
  .withColumn("Trunc",
     date_trunc("week", 'DateTime))
  .show(false)


