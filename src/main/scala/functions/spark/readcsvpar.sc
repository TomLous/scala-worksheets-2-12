import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.log4j.{ Level, Logger }

//Logger.getLogger("root").setLevel(Level.DEBUG)


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.ui.showConsoleProgress", true)
  //  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val flightData2015 = spark
  .read
  .option("inferSchema", "true")
  .option("header", "true")
  .csv("src/main/resources/files/csv/ids.csv")


flightData2015.show()