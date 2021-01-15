import scala.util.{Failure, Success, Try}

final case class Ex1(message: String) extends Exception(message)
final case class Ex2(message: String) extends Exception(message)

def main(i: Int): String = {
  if (i == 1)
    throw Ex1("error1")
  else if (i == 2)
    throw Ex2("error2")
  else "ok1"
}
def alternative(t: Boolean): String = if (t) throw Ex1("error B") else "ok2"

val i = 0
val b = true

val res: Either[Throwable, String] =
  Try(main(i))
    .recoverWith {
      case _: Ex1 => Try(alternative(b))
      case e => Failure(e)
    }.toEither


