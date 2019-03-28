
// Scala example
import com.github.vickumar1981.stringdistance.Strategy
import com.github.vickumar1981.stringdistance.StringDistance._
import com.github.vickumar1981.stringdistance.StringSound._
import com.github.vickumar1981.stringdistance.impl.{ConstantGap, LinearGap}

def checkAll(s1: String, s2: String) = {
  // Cosine Similarity
  Map(
    "Cosine" -> Cosine.score(s1, s2, Strategy.splitWord),
    "Damerau" -> Damerau.score(s1, s2),
    "DiceCoefficient" -> DiceCoefficient.score(s1, s2),
    "Hamming" -> Hamming.score(s1, s2),
    "Jaccard" -> Jaccard.score(s1, s2),
    "Jaro" -> Jaro.score(s1, s2),
    "JaroWinkler" -> JaroWinkler.score(s1, s2),
    "Levenshtein" -> Levenshtein.score(s1, s2),
    "NeedlemanWunsch" -> NeedlemanWunsch.score(s1, s2, ConstantGap()),
    "NGram1" -> NGram.score(s1, s2, 1),
    "NGram2" -> NGram.score(s1, s2, 2),
    "Overlap1" -> Overlap.score(s1, s2, 1),
    "Overlap2" -> Overlap.score(s1, s2, 2),
    "SmithWaterman" -> SmithWaterman.score(s1, s2, (LinearGap(gapValue = -1), Integer.MAX_VALUE)),
    "SmithWatermanGotoh" -> SmithWatermanGotoh.score(s1, s2, ConstantGap()),
    "Tversky" -> Tversky.score(s1, s2),
//    "Metaphone" -> Metaphone.score(s1, s2),
//    "Soundex" -> Soundex.score(s1, s2),
  )
}

def checks = List(
  ("4547 COUNTY ROAD 601", "4547 COUNTRY ROAD 601"),
  ("WASHINGTON STATE UNIV., INT'L PROGS-GLOBAL SERV", "WASHINGTON STATE UNIV., INT'L PROGS-GLOBAL SERVICES BRYAN HALL 108, PO..."),
  ("PO BOX 388 2 HARBOR SQUARE", "2 HARBOUR SQUARE"),
  ("728 WEST SECOND STR.", "728 W. 2ND. ST."),
  ("728 WEST SECOND STR.", "728 W. 2ND STREET P.O. BOX 249  CROWLEY, LA 70527"),
  ("181 NORTH 11TH STREET SUITE 307", "181 NORTH 11TH STREET"),
  ("191 NORTH 11TH STREET", "181 NORTH 12TH STREET"),
)


checks.foreach{
  case (a, b) =>
    println("")
    println("")
    println(s"$a <=> $b")
    checkAll(a, b).toList.sortBy(-_._2).foreach{
      case (m, s) =>
        println(s"$m: $s")
    }



}