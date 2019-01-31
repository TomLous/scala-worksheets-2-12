val users = Seq("","a","b","c","","d")
val userGroups1 = List("a","c")

users.find(userGroups1.contains) match {
  case Some(owner) =>
    println("ff")
    true
  case None =>
    println("gg")
    false
}

