import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.TimestampType

import java.sql.Timestamp


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test-millis")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val df = List(
  (1662537685, "a"),
  (1662538685, "b"),
  (1672538685, "c"),
  (1632538685, "d"),
  (1632539685, "e"),
).toDF("ingested_at", "name")




val newDf = df
//  .withColumn("ingested_at2", unix_timestamp(from_unixtime($"ingested_at")).multiply(1000) + 222)
//  .withColumn("now", unix_timestamp(current_timestamp()).multiply(1000) + 111)
//  .withColumn("ingested_at3", concat(from_unixtime($"ingested_at", "yyyy-MM-dd HH:mm:ss"),lit(".000")).cast(TimestampType))
//  .withColumn("ingested_at4", concat(current_timestamp(),lit(".000")).cast(TimestampType))
  .withColumn("ingested_at", current_timestamp())
  .withColumn("ingested_at2", lit(new Timestamp(System.currentTimeMillis())))


newDf.show(false)
newDf.printSchema()

