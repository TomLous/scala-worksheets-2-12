import cats.implicits._
//import cats.syntax.traverse._

class Test[T](a: T){



}

val listOfEithers1:List[Either[String, Int]] = List(Right(1), Left("Error"), Right(4))
val listOfEithers2:List[Either[String, Int]] = List(Right(1), Right(10), Right(4))
val listOfEithers3:List[Either[String, Test[Int]]] = List(Right(new Test(1)), Right(new Test(10)), Right(new Test(4)))
//val listOfOptions:List[Option[Int]] = List(Some(1), None, Some(4))

//listOfOptions.sequence
listOfEithers1.sequence
listOfEithers2.sequence
listOfEithers3.sequence