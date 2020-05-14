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


case class KeyedItem[+T <: Item](key: String, value: T)


case class ItemC(name: String, value: Double,  num: Int ) extends Item{
  override val key: String = name
}