import java.sql.Date

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import shapeless.ops.hlist.Partition


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._


val data = List(
  ("XSC", "1986-05-21", 44.7530),
  ("XSC", "1986-05-22", 44.7530),
  ("XSC", "1986-05-23", 23.5678),
  ("TM", "1982-03-08", 22.2734),
  ("TM", "1982-03-09", 22.1941),
  ("TM", "1982-03-10", 22.0847),
  ("TM", "1982-03-11", 22.1741),
  ("TM", "1982-03-12", 22.1840),
  ("TM", "1982-03-15", 22.1344),
).toDF("id", "timestamp", "feature")
  .withColumn("timestamp", to_date('timestamp))



// Do some complexComputation (now running total)
val complexComputationUDF = udf((list: Seq[Row]) => {
  list
    .map(row => (row.getString(0), row.getDate(1).getTime, row.getDouble(2)))
    .sortBy(-_._2)
    .foldLeft(0.0) {
      case (acc, (id, timestamp, feature)) => acc + feature
    }
})

val windowAll = Window.partitionBy("id")
val windowRunning = Window.partitionBy("id").orderBy("timestamp")


val newData = data
  // I assuming thatyou need id,timestamp & feature for the complex computattion. So I create a struct
  .withColumn("record", struct('id, 'timestamp, 'feature))
  // Collect all records in the partition as a list of tuples and pass them to the complexComupation
  .withColumn("computedValueAll",
  complexComputationUDF(collect_list('record).over(windowAll)))
  // Collect records in a time ordered windows in the partition as a list of tuples and pass them to the complexComupation
  .withColumn("computedValueRunning",
  complexComputationUDF(collect_list('record).over(windowRunning)))


newData.show(false)

