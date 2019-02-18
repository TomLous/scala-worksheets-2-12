
/*
 each identifier has some keys `generated`
 The trick is to bundle all identiefs with an overlapping key and return the unique identifiers
 In this case the result should be row1 (or 2,3,5) and row4
  */
val identifiers = List(
  ("row1", List("abc", "cde", "efg")),
  ("row2", List("cde", "hij")),
  ("row3", List("jkl", "hij")),
  ("row4", List("xyz", "stu")),
  ("row5", List("rpq", "abc"))
)

val sets = identifiers
  .toMap
  .mapValues(_.toSet)
  .toList
  .sortBy(_._1)


sets.foldLeft(
  Map.empty[String, Set[String]]
){
  case (acc, (key, set)) =>
    acc.partition{
      case (_, otherset) => otherset.intersect(set).nonEmpty
    } match {
      case (nil, other) if nil.isEmpty =>
        other + (key -> set)
      case (intersected, other) =>
        other + (key -> (intersected.flatMap(_._2).toSet ++ set))
    }
}

//for {
//  (i, seti) <- sets
//  (j, setj) <- sets
//  if i!=j && seti.intersect(setj).nonEmpty
//} yield (i, j)