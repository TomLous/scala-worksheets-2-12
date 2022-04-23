
object HouseFactory extends App{
  sealed trait Appliance
  sealed case class Shower(water: Water) extends Appliance
  sealed case class Kitchen(electricity: Electricity) extends Appliance
  sealed case class Radiator(gas: Gas) extends Appliance

  sealed trait Input
  sealed case class Water( isHot: Boolean) extends Input
  sealed case class Electricity(level: Int) extends Input
  sealed case class Gas(provider: String) extends Input

  val water = Water(true)
  val elex = Electricity(3)
  val gas = Gas("whatever")
  val inputList = List(water, elex, gas)

  def create(input: Input): Appliance =  input match {
    case i:Water => Shower(i)
    case i:Electricity => Kitchen(i)
    case i:Gas => Radiator(i)
  }

  inputList
    .map(create)
    .foreach(println)
}

HouseFactory.main(new Array[String](0))

object HouseFactory2 extends App{
  abstract class Input
  case class Water(isHot: Boolean) extends Input
  case class Electricity(level: Int) extends Input
  case class Gas(provider: String) extends Input

  case class Appliance[T <: Input](input: T)
  type Shower = Appliance[Water]
  type Kitchen = Appliance[Electricity]
  type Radiator = Appliance[Gas]

  val water = Water(true)
  val elex = Electricity(3)
  val gas = Gas("whatever")
  val inputList = List(water, elex, gas)

  inputList
    .map(Appliance(_))
    .foreach{
      case x:Shower => print("Shower", x)
      case x:Kitchen => print("Kitchen", x)
      case x:Radiator => print("Radiator", x)
      case x=> print("??", x)
    }







}

HouseFactory2.main(new Array[String](0))