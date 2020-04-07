import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.collection.JavaConverters._

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.ui.showConsoleProgress", true)
  .getOrCreate()

def removeWhiteSpace(s: Any): String = Option(s)
  .map(_.toString.replace(" ", ""))
  .getOrElse("_")

val raw = spark
  .read
  .csv("src/main/resources/files/csv/dirty-format.csv")

val encoder = RowEncoder(raw.schema)

val df = raw
  .mapPartitions(_.drop(3))(encoder)
  .collect()
  .toList match {
  case header :: rows =>
    spark
      .createDataFrame(rows.asJava,
        StructType(header
          .toSeq
          .map(removeWhiteSpace)
          .map(StructField(_, StringType))))
}

df.show()


/*
+----+----+-------+----------+----+----+
|   _|   1|tomTest|ohipipeloi|   _|   _|
+----+----+-------+----------+----+----+
|   a|   2|     11|        aa|null|null|
|   b|   3|     22|        bb|null|null|
|   c|   4|     33|        cc|null|null|
|null|null|   null|      null|null|null|
|null|null|   null|      null|null|null|
|dddd| ddd|    ddd|       ddd| ddd|null|
+----+----+-------+----------+----+----+
 */