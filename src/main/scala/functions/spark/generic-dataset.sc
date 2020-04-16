import functions.spark.{Item, ItemA, ItemB, KeyedItem}
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}
import scala.reflect.runtime.universe._

val spark = SparkSession.builder()
  .master("local[2]")
  .getOrCreate()

import spark.implicits._

val aDS: Dataset[ItemA] = List(
  ItemA("a", 12.3),
  ItemA("b", 13.4),
  ItemA("c", 14.5)
).toDS()

val bDS: Dataset[ItemB] = List(
  ItemB(11, 12.3),
  ItemB(22, 13.4),
  ItemB(33, 14.5)
).toDS()

def toKeyValueDataset[T <: Item](dataset: Dataset[T])
                                (implicit encoder: Encoder[KeyedItem[T]]) // <-- fixes encoder issues
: Dataset[KeyedItem[T]] = {
  dataset
    .map(item => KeyedItem(item.key, item))
}

val dummyDs: DataFrame = List(
 ("dummy1", 1, 11.1),
 ("dummy2", 2, 22.2),
 ("dummy3", 3, 33.3),
).toDF("id", "int", "dbl")

val datasets:List[Dataset[_]] =
  List(
    toKeyValueDataset(aDS),
    toKeyValueDataset(bDS),
    dummyDs
  )

// do a lot of stuff

def sinkKeyValueDatasets[T: TypeTag](dataset: Dataset[T]): Unit ={
  val ds = typeOf[T] match {
    case t if t.typeConstructor =:= typeOf[KeyedItem[_]].typeConstructor =>
      dataset.map(_.value)
    case _ =>
      dataset
  }

  ds.show()
}


def sinkRegularDatasets[T: TypeTag](dataset: Dataset[T]): Unit ={
  val ds = typeOf[T] match {
    case t if t.typeConstructor =:= typeOf[KeyedItem[_]].typeConstructor =>
      dataset.map(_.value)
    case _ =>
      dataset
  }

  ds.show()
}



datasets.foreach(sinkKeyValueDatasets(_))
