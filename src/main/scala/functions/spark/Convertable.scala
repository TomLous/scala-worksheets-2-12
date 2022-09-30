package functions.spark

import java.sql.Timestamp

abstract class Convertable[T <: Product] extends Product{
  def convert(ingestedAt: Timestamp, ingestedId:String):T
}
