import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

// @see https://stackoverflow.com/questions/48754978/pyspark-dataframe-recursive-column

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

case class Record(A: Int, b: Double, F_0: Option[Double], F_1: Option[Double], F_2: Double) {
//  def calcF(previousF: Option[Double]) = {
//    F_0 match {
//      case Some(init) => init + F_1 + F_2
//      case None => previousF.map(_ * 0.25).getOrElse(0.0) + F_1 + F_2
//    }
//  }
}

org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)


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

val df = List((1, 1.000540895285929161),
  (2, 1.097289726627339219),
  (3, 0.963925596369865420),
  (4, 0.400642772674179290),
  (5, 1.136213095583983134),
  (6, 1.563124989279187345),
  (7, 0.924395764582530139),
  (8, 0.833237679638091343),
  (9, 1.381905515925928345),
  (10, 1.315542676739417356),
  (11, 0.496544353345593242),
  (12, 1.075150956754565637),
  (13, 0.912020266273109506),
  (14, 0.445620998720738948),
  (15, 1.440258342829831504),
  (16, 0.929157554709733613),
  (17, 1.168496273549324876),
  (18, 0.836936489952743701),
  (19, 0.629466356196215569),
  (20, 1.145973619225162914),
  (21, 0.987205342817734242),
  (22, 1.442075381077187609),
  (23, 0.958558287841447591),
  (24, 0.924638906376455542),
).toDF("A", "b")

val orderedWindow = Window.orderBy('a)

df
  .select(
    'A,
    'b,
    when('a === 1, 'b * 0.25).as("F_0"), // b(I) * 0.25
    (lead('b, 1).over(orderedWindow) * 0.5).as("F_1"), // b(I + 1) * 0.50
    ('b * 0.25).as("F_2") // b(I) * 0.25
  )
  .as[Record]
  .collect()
  .toList
  .sliding(2, 1)
  .foreach(
    println
  )


//  .withColumn("F_0", 'F_init)
//  .withColumn("F_0", lag('F_0, 1).over(orderedWindow))

//
//  .withColumn( "F_3", when('a === 1,'b * 0.25).otherwise(lit(null)))
//  .withColumn( "F_3", lag('F_3, 1).over(orderedWindow) * 0.25)


//  .withColumn("F",
//    coalesce(lag('F, -1).over(orderedWindow),
//
//  )
//    .show(100, false)


