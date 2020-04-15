package functions.spark

trait Item {
  val key: String
}

case class ItemA(name: String, value:Double) extends Item{
  override val key: String = name
}

case class ItemB(num: Int, value:Double) extends Item{
  override val key: String = num.toString
}
