import scala.util.{Failure, Success, Try}

def handleTry[T](event:Try[T]) = event match {
  case s: Success[T] => println("success"); s
  case f: Failure[T] => println("failure"); f
}

handleTry(Try("d".toInt))
handleTry(Try("3".toInt))