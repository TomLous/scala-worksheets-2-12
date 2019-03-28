import scala.util.matching.Regex

// https://regex101.com/r/dfKYH7/2
val pattern:Regex =
  """^.*?(/year=(\d+)(.*/month=(\d+)(.*/day=(\d+))?)?)?/?$""".r(null, "Y", null, "M", null, "D")

val tests = List("/test/data",
  "/test/data/year=2001",
  "/test/data/year=2001/",
  "/test/data/year=2001/month=03",
  "/test/data/year=2001/month=03/",
  "/test/data/year=2001/fff/month=03/",
  "/test/data/year=2001/fff/month=03/day=04",
  "/test/data/year=2001/month=03/day=04/",
  "/test/data/year=2001/day=04/",
  "/test/data/year=2001/fff/day=04/")



tests.foreach(s => {

  val matches = pattern
    .findAllIn(s)
    .matchData
    .map(m =>
      (Option(m.group("Y")), Option(m.group("M")),Option(m.group("D")))
    ).toList

  println(s + " : " + matches)


}

)