import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
//  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._


val sourceFile = "/tmp/testoverwrite/A"

val init = List((1, "a", 0), (2, "B", 0), (3, "C", 0)).toDF("A", "B", "count")
init.write.mode(SaveMode.Overwrite).parquet(sourceFile)

val A = spark.read.parquet(sourceFile)
A.show()