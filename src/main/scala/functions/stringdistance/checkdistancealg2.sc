//
//// Scala example
//
//import com.rockymadden.stringmetric.similarity._
//
//def checkAll(s1: String, s2: String) = {
//  // Cosine Similarity
//  Map(
//    "DiceSorensenMetric1" -> DiceSorensenMetric(1).compare(s1, s2),
//    "DiceSorensenMetric2" -> DiceSorensenMetric(2).compare(s1, s2),
//    "JaccardMetric1" -> JaccardMetric(1).compare(s1, s2),
//    "JaccardMetric2" -> JaccardMetric(2).compare(s1, s2),
//    "JaroMetric" -> JaroMetric.compare(s1, s2),
//    "JaroWinklerMetric" -> JaroWinklerMetric.compare(s1, s2),
//    "NGramMetric1" -> NGramMetric(1).compare(s1, s2),
//    "NGramMetric2" -> NGramMetric(2).compare(s1, s2),
//    "OverlapMetric1" -> OverlapMetric(1).compare(s1, s2),
//    "OverlapMetric2" -> OverlapMetric(2).compare(s1, s2),
//    "RatcliffObershelpMetric" -> RatcliffObershelpMetric.compare(s1, s2),
//    "WeightedLevenshteinMetric" -> WeightedLevenshteinMetric(0.1, 0.1, 0.2).compare(s1, s2),
//
//  )
//}
//
//def checks = List(
//  ("4547 COUNTY ROAD 601", "4547 COUNTRY ROAD 601"),
//  ("WASHINGTON STATE UNIV., INT'L PROGS-GLOBAL SERV", "WASHINGTON STATE UNIV., INT'L PROGS-GLOBAL SERVICES BRYAN HALL 108, PO..."),
//  ("PO BOX 388 2 HARBOR SQUARE", "2 HARBOUR SQUARE"),
//  ("728 WEST SECOND STR.", "728 W. 2ND. ST."),
//  ("728 WEST SECOND STR.", "728 W. 2ND STREET P.O. BOX 249  CROWLEY, LA 70527"),
//  ("181 NORTH 11TH STREET SUITE 307", "181 NORTH 11TH STREET"),
//  ("191 NORTH 11TH STREET", "181 NORTH 12TH STREET"),
//)
//
//
//checks.foreach{
//  case (a, b) =>
//    println("")
//    println("")
//    println(s"$a <=> $b")
//    checkAll(a, b).flatMap{
//      case (k, Some(v)) => Some(k -> v)
//      case _ => None
//    }.toList.sortBy(-_._2).foreach{
//      case (m, s) =>
//        println(s"$m: $s")
//    }
//
//
//
//}