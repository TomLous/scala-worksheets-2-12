// First unique char in a  String
val string = "dit is een hele lange string die veel woorden bevat"


// Readable, but multiple loops
string
  .zipWithIndex
  .toList
  .groupBy(_._1)
  .mapValues(_.unzip)
  .mapValues(t => (t._1.length, t._2.min))
  .filter(_._2._1 == 1)
  .mapValues(_._2)
  .toList
  .sortBy(_._2)
  .headOption
  .map(_._1)


// Keep track using 2 maps
(string
  .foldLeft((collection.mutable.Map.empty[Char, Int], collection.mutable.Map.empty[Char, Int].withDefaultValue(0))) {
    case ((firstIndex, counters), c) =>
      firstIndex.get(c) match {
        case None => firstIndex(c) = firstIndex.size
        case _ => Unit
      }
      counters(c) += 1
      (firstIndex, counters)
  }
match {
  case (firstIndex, counters) => counters.toList.filter(_._2 == 1).map { x =>
    x._1 -> firstIndex.getOrElse(x._1, Int.MaxValue)
  }
})
  .sortBy(_._2)
  .headOption
  .map(_._1)


// Keep track using 1 map
string
  .foldLeft(collection.mutable.Map.empty[Char, (Int, Int)]) {
    case (counters, c) =>
      val current:Option[(Int, Int)] = counters.get(c)

      counters(c) = current match {
        case None => (counters.size, 1)
        case Some((pos, count)) => (pos, count+1)
      }
      counters
  }.filter(_._2._2 == 1)
  .toList
  .sortBy(_._2._1)
  .headOption
  .map(_._1)


