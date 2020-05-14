import functions.spark.ItemC
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.catalyst.encoders.RowEncoder


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.ui.showConsoleProgress", true)
  .getOrCreate()

import spark.implicits._

val data = List(
  ("a", 1.1, 100, "2020-05-01"),
  ("b", 2.1, 200, "2020-05-02"),
  ("c", 3.1, 300, "2020-05-03"),
  ("d", 4.1, 400, "2020-05-04")
).toDF("name", "value", "num", "date")

data.show()
data.printSchema()

val dataset = data.as[ItemC].map(identity)

dataset.show()
dataset.printSchema()
