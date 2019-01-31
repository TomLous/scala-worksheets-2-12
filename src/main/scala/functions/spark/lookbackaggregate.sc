import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val data = Seq(("1", "201706", "5"), ("1", "201707", "10"), ("2", "201604", "12"), ("2", "201601", "15")).toDF("id", "yyyyMM", "amount")

val groupByWindow = Window.partitionBy($"yearmonth")
val orderByWindow = Window.orderBy('yearmonth)

val ds = data
  // convert the string to an actual date
  .withColumn("yearmonth", to_date('yyyyMM, "yyyyMM"))
  // for each record create 2 additional in the future (with 0 amount)
  .select(
  explode(array(
    // org record
    struct('id, date_format('yearmonth, "yyyyMM").as("yearmonth"), 'amount),
    // 1 month in future
    struct('id, date_format(add_months('yearmonth, 1), "yyyyMM").as("yearmonth"), lit(0).as("amount")),
    // 2 months in future
    struct('id, date_format(add_months('yearmonth, 2), "yyyyMM").as("yearmonth"), lit(0).as("amount")),
  )).as("record"))
  // keep 1 record per month
  .groupBy($"record.yearmonth")
  .agg(
    min($"record.id").as("id"),
    sum($"record.amount").as("amount")
  )
  // final structure (with lag fields)
  .select(
    'id,
    'yearmonth,
    'amount,
    lag('yearmonth, 1).over(orderByWindow).as("yearmonth-1"),
    lag('amount, 1, 0).over(orderByWindow).as("amount2"),
    lag('yearmonth, 2).over(orderByWindow).as("yearmonth-2"),
    lag('amount, 2, 0).over(orderByWindow).as("amount3"),
  )
  .orderBy('yearmonth.desc)

ds.orderBy('yearmonth.desc).show(false)

//val w = Window.orderBy("yyyyMM")
//
//data.select(
//  'id,
//  'yyyyMM.as("yearmonth"),
//  'amount,
//  lag("yearmonth", 1, 0).over(w).as("yearmonth-1"),
//  lag("amount", 1, 0).over(w).as("amount"),
//)
//val leadDf = df.withColumn("new_col", lag("volume", 1, 0).over(w))
//
