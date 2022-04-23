// How to arrange 8 numbers in a 2/4/2 matrix so that each number is > 1 away from each adjacent postition (hor, vert, diag)


val matrixDefinition = List(
  List(None,    Some(0), Some(1), None),
  List(Some(2), Some(3), Some(4), Some(5)),
  List(None,    Some(6), Some(7), None),
)

val adjacencyIndex:List[(Int, Int)] = {
  matrixDefinition.zipWithIndex.flatMap{
    case(row, rowIndex) =>
      row.zipWithIndex.flatMap{
        case(Some(cell), colIndex) =>
          List(
            (rowIndex - 1, colIndex - 1),
            (rowIndex - 1, colIndex),
            (rowIndex - 1, colIndex + 1),
            (rowIndex, colIndex - 1),
            (rowIndex, colIndex + 1),
            (rowIndex + 1, colIndex - 1),
            (rowIndex + 1, colIndex),
            (rowIndex + 1, colIndex + 1),
          ).filter{
            case (row, col) => row >= 0 &&
              row < matrixDefinition.size &&
              col >= 0 &&
              col < matrixDefinition(row).size
          }.flatMap{
            case (row, col) => matrixDefinition(row)(col)
          }.map(v => (v,cell))
        case _ =>
          Nil
      }
  }.sortBy(_._1)
}

def printMatrixSolution(solution:List[Int]) = {
  for (row <- matrixDefinition) {
    for (cell <- row) {
      print(cell.map(solution(_)).getOrElse(" ") + " ")
    }
    println()
  }
  println("------")
}


def isValidSolution(solution:List[Int]):Boolean = {
  adjacencyIndex.forall{
    case (pos1, pos2) => math.abs(solution(pos1) - solution(pos2)) > 1
  }
}



val possibleSolutions = List(1,2,3,4,5,6,7,8).permutations.toList

possibleSolutions.filter(isValidSolution).foreach(printMatrixSolution)

//printMatrixSolution(matrixDefinition, List(0,0,0,0,0,0,0,0))


