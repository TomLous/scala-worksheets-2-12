import functions.spark.{DirtyMapper, DirtyResult}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.catalyst.encoders.RowEncoder


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.ui.showConsoleProgress", true)
  .getOrCreate()

import spark.implicits._

val raw = spark
  .read
  .csv("src/main/resources/files/csv/dirty-format.csv")

val encoder = RowEncoder(raw.schema)

val df:Dataset[DirtyResult] = raw
  .mapPartitions(_.drop(3))(encoder)
  .flatMap(row => DirtyMapper.mapRowsToResults(row))


df.show()

df.printSchema()


/*
+---+---+----+-----+
| id|num|text|value|
+---+---+----+-----+
|  2| 11|  aa| 12.3|
|  3| 22|  bb| 23.4|
|  4| 33|  cc| 25.6|
+---+---+----+-----+
 */