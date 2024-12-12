import aoc.utils.graph.adjacent
import java.io.File

val xx = mutableListOf<Pair<Int, Int>>()
val mm = mutableListOf<Pair<Int, Int>>()
val aa = mutableListOf<Pair<Int, Int>>()
val ss = mutableListOf<Pair<Int, Int>>()

fun main(args: Array<String>) {
   val input = File(args.first()).readLines()
   input.forEachIndexed { y, line ->
      line.forEachIndexed { x, value ->
         val coords = Pair<Int, Int>(x, y)
         when (value) {
            'X' -> xx.add(coords)
            'M' -> mm.add(coords)
            'A' -> aa.add(coords)
            'S' -> ss.add(coords)
            else -> println("Not relieant char")
         }
      }
   }

   println("Solution 1: ${solution1()}")
   println("Solution 2: ${solution2()}")
}

private fun solution1(): Int {
   return findPaths().size
}

private fun solution2(): Int {
   val paths = findDiagPaths()
   var crosses = mutableSetOf<MutableList<Pair<Int, Int>>>()

   // if two paths have the same A coord, they might cross
   for (index in paths.indices) {
      val aCoord = paths[index][1]
      for (p in paths.filter { it != paths[index] }) {
         if (p[1] == aCoord) {
            // if the y in either the first or last is the same as aCood, they don't cross
            // if the x in either the first or last is the same as aCoord, they don't cross
            if (p.first().first != aCoord.first && p.last().first != aCoord.first &&
               p.first().second != aCoord.second && p.last().second != aCoord.second) {
               crosses.add(p)
            }
         }
      }
   }

   return crosses.groupingBy { it[1] }.eachCount().filterValues{ it == 2 }.size
}

private fun findDiagPaths(): MutableList<MutableList<Pair<Int, Int>>> {
   var paths = mutableListOf<MutableList<Pair<Int, Int>>>()
   for (mmCoord in mm) {
      val aaNeighbors = adjacent(mmCoord, true).intersect(aa)
      for (aaCoord in aaNeighbors) {
         val ssCoord = expectDiag(mmCoord, aaCoord)
         if (ss.contains(ssCoord)) {
            val masPath = mutableListOf<Pair<Int, Int>>()
            masPath.add(mmCoord)
            masPath.add(aaCoord)
            masPath.add(ssCoord)
            paths.add(masPath)
         }
      }
   }
   return paths
}

private fun findPaths(): MutableList<MutableList<Pair<Int, Int>>> {
   var paths = mutableListOf<MutableList<Pair<Int, Int>>>()
   for (xxCoord in xx) {
      val mmNeighbors = adjacent(xxCoord, true).intersect(mm)
      for (mmCoord in mmNeighbors) {
         val aaCoord = expectDirection(xxCoord, mmCoord)
         if (aa.contains(aaCoord)) {
            val ssCoord = expectDirection(mmCoord, aaCoord)
            if (ss.contains(ssCoord)) {
               val xmasPath = mutableListOf<Pair<Int, Int>>()
               xmasPath.add(xxCoord)
               xmasPath.add(mmCoord)
               xmasPath.add(aaCoord)
               xmasPath.add(ssCoord)
               paths.add(xmasPath)
            }
         }
      }
   }
   return paths
}

private fun expectDirection(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Pair<Int, Int> {
   if (p2.first < p1.first && p2.second > p1.second) {
      // diag left down
      return Pair<Int, Int>(p2.first - 1, p2.second + 1)
   } else if (p2.first > p1.first && p2.second > p1.second) {
      // diag right down
      return Pair<Int, Int>(p2.first + 1, p2.second + 1)
   } else if (p2.first < p1.first && p2.second < p1.second) {
      // diag left up
      return Pair<Int, Int>(p2.first - 1, p2.second - 1)
   } else if (p2.first > p1.first && p2.second < p1.second) {
      // diag right up
      return Pair<Int, Int>(p2.first + 1, p2.second - 1)
   } else if (p2.first < p1.first && p2.second == p1.second) {
      // left
      return Pair<Int, Int>(p2.first - 1, p2.second)
   } else if (p2.first > p1.first && p2.second == p1.second) {
      // right
      return Pair<Int, Int>(p2.first + 1, p2.second)
   } else if (p2.first == p1.first && p2.second < p1.second) {
      // up
      return Pair<Int, Int>(p2.first, p2.second - 1)
   } else {
      // down
      return Pair<Int, Int>(p2.first, p2.second + 1)
   }
}

private fun expectDiag(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Pair<Int, Int> {
   if (p2.first < p1.first && p2.second > p1.second) {
      // diag left down
      return Pair<Int, Int>(p2.first - 1, p2.second + 1)
   } else if (p2.first > p1.first && p2.second > p1.second) {
      // diag right down
      return Pair<Int, Int>(p2.first + 1, p2.second + 1)
   } else if (p2.first < p1.first && p2.second < p1.second) {
      // diag left up
      return Pair<Int, Int>(p2.first - 1, p2.second - 1)
   } else {
      // diag right up
      return Pair<Int, Int>(p2.first + 1, p2.second - 1)
   }
}
