


type AResource = Int

def testA(
    s: String
)(implicit aResource: AResource): (Double) => (String, Int) =
  (d: Double) => {
    (s + d, d.toInt * aResource)
  }

implicit val ar: AResource = 3

val fA = testA("org: ")
fA(3.1415)

val fA2 = testA("org2: ")(ar)(1.123)

def testC(s: String): (Double) => (AResource) => (String, Int) =
  (d: Double) => {
    implicit aResource: AResource =>
      {
        (s + d, d.toInt * aResource)
      }
  }

val c: (String, Int) = testC("org: ")(2.4)(ar)

def testD(s: String): (Double) => (String, Int) = {
  def f(d: Double)(implicit aResource: AResource): (String, Int) =
    (s + d, d.toInt * aResource)

  f _

}

val d: (String, Int) = testD("org: ")(2.4)


