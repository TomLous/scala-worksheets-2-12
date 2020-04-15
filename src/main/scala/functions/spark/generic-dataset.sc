import functions.spark.{Item, ItemA, ItemB}
import org.apache.spark.sql.{Dataset, Encoder, SparkSession}

val spark = SparkSession.builder()
  .master("local[2]")
  .getOrCreate()

import spark.implicits._

val aDS:Dataset[ItemA] = List(
  ItemA("a", 12.3),
  ItemA("b", 13.4),
  ItemA("c", 14.5)
).toDS()

val bDS:Dataset[ItemB] = List(
  ItemB(11, 12.3),
  ItemB(22, 13.4),
  ItemB(33, 14.5)
).toDS()

def toKeyValueDataset[T <: Item](dataset: Dataset[T])(implicit encoder:Encoder[(String, T)]):Dataset[(String, T)] = {
  dataset
    .map(item => (item.key, item))
}

val aKeyed = toKeyValueDataset(aDS)
val bKeyed = toKeyValueDataset(bDS)

aKeyed.show()
bKeyed.show()
