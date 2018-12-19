val segment = List(1,2,3,4,4,4,5);

for (i <- 0 until segment.size) yield s"segment/$i"

0 until segment.size map(i => s"segment/$i")
