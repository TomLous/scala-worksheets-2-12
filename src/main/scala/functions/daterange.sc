import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit._
import scala.util.Try

def collectPartitionPathsSafe(
    basePath: String,
    startPeriod: String,
    endPeriod: String
): Either[Throwable, Seq[String]] = {

  val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00")
  val pathFormatter = DateTimeFormatter.ofPattern(
    s"""'$basePath/year='YYYY'/month='MM'/day='dd"""
  )

  for {
    start <- Try(LocalDateTime.parse(startPeriod, inputFormatter)).toEither
    end <- Try(LocalDateTime.parse(endPeriod, inputFormatter)).toEither
    dayRange = DAYS.between(start, end).toInt
    days <- Either.cond(
      dayRange > 0,
      0 to dayRange,
      new IllegalArgumentException(
        s"End date $endPeriod should be after $startPeriod"
      )
    )
    paths = for {
      day <- days
      path = pathFormatter.format(start.plusDays(day))
    } yield path
  } yield paths

}

def collectPartitionPaths(
    basePath: String,
    startPeriod: String,
    endPeriod: String
): Seq[String] = {
  collectPartitionPathsSafe(basePath, startPeriod, endPeriod).fold(
    throw _,
    identity
  )
}

val x = collectPartitionPaths(
  "/dummy/path/is",
  "2021-01-01 21:00",
  "2021-03-01 11:00"
)

x.foreach(println)
