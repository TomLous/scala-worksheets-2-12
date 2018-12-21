import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val df1 = List(
  "n3_testindia1",
  "n2_stagamerica2",
  "n1_prodeurope2"
).toDF("location1")
//  .withColumn("extract", regexp_extract('location1, """[^_]+_(.*)""", 1))
  .withColumn("extract",   expr("substring(location1, 4, length(location1)-3)")
)

//df1.show()

val df2 = List(
  "test-india-1",
  "stag-america-2",
  "prod-europe-2"
).toDF("loc1")
//  .withColumn("replace", translate('loc1, "-", ""))

//df2.show()

val joindf = df1.join(df2,
  regexp_extract(df1("location1"), """[^_]+_(.*)""", 1)
    === translate(df2("loc1"), "-", ""))

val joindf2 = df1.join(df2,
//  expr("substring(location1, 4, length(location1)-3)")
    df1("location1").substr(lit(4), length(df1("location1")))
    === translate(df2("loc1"), "-", ""))


//expr("substring(value, 1, length(value)-1)")



joindf.show()
joindf2.show()
