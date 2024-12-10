import aoc.utils.graph.manhattanDistance
import java.io.File
import kotlin.math.abs

val antennas = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

var XBounds = 0..0
var YBounds = 0..0

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()

   lines.forEachIndexed { y, line ->
      line.forEachIndexed { x, value ->
         if (value != '.') {
            if (antennas.contains(value)) {
               antennas.get(value)?.add(Pair<Int, Int>(x, y))
            } else {
               val positions = mutableListOf<Pair<Int, Int>>()
               positions.add(Pair<Int, Int>(x, y))
               antennas.put(value, positions)
            }
         }
      }
   }

   YBounds = 0..lines.size - 1
   XBounds = 0..lines[0].length - 1

   println("Solution 1: ${solution1()}")
   println("Solution 2: ${solution2()}")
}

private fun solution1(): Int {
   val antinodes = mutableSetOf<Pair<Int, Int>>()

   antennas.values.forEach { coordsList ->
      for (k in 0 until coordsList.size) {
         var p1 = coordsList[k]

         for (i in (k + 1)..coordsList.size - 1) {
            var p2 = coordsList[i]

            var xDiff = abs(p2.first - p1.first)
            var yDiff = abs(p2.second - p1.second)

            var anti1: Pair<Int, Int>
            var anti2: Pair<Int, Int>

            if (p1.first < p2.first && p1.second < p2.second) {
               anti1 = Pair<Int, Int>(p1.first - xDiff, p1.second - yDiff)
               anti2 = Pair<Int, Int>(p2.first + xDiff, p2.second + yDiff)
            } else {
               anti1 = Pair<Int, Int>(p1.first + xDiff, p1.second - yDiff)
               anti2 = Pair<Int, Int>(p2.first - xDiff, p2.second + yDiff)
            }

            if (checkInBounds(XBounds, YBounds, anti1)) {
               antinodes.add(anti1)
            }
            if (checkInBounds(XBounds, YBounds, anti2)) {
               antinodes.add(anti2)
            }
         }
      }
   }

   return antinodes.size
}

private fun solution2(): Int {
   val antinodes = mutableSetOf<Pair<Int, Int>>()

   antennas.values.forEach { coordsList ->
      for (k in 0 until coordsList.size) {
         var p1 = coordsList[k]

         for (i in (k + 1)..coordsList.size - 1) {
            var p2 = coordsList[i]

            // go in both direction
            antinodes.addAll(createAntinodes(p1, p2))
            antinodes.addAll(createAntinodes(p2, p1))
         }
      }
   }

   return antinodes.size
}

private fun createAntinodes(p1: Pair<Int, Int>, p2: Pair<Int, Int>): List<Pair<Int, Int>> {
   var antinodes = mutableListOf<Pair<Int, Int>>()
   antinodes.add(p1)
   antinodes.add(p2)
   var newANode = expectDirection(p1, p2)
   if (checkInBounds(XBounds, YBounds, newANode)) {
      antinodes.addAll(createAntinodes(p2, newANode))
   }

   return antinodes
}

private fun checkInBounds(xBounds: IntRange, yBounds: IntRange, p: Pair<Int, Int>): Boolean {
   return p.first in xBounds && p.second in yBounds
}

// Borrowed from Day 04, added the xDiff and yDiff instead of the hard coded value of 1
private fun expectDirection(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Pair<Int, Int> {
   var xDiff = abs(p2.first - p1.first)
   var yDiff = abs(p2.second - p1.second)

   if (p2.first < p1.first && p2.second > p1.second) {
      // diag left down
      return Pair<Int, Int>(p2.first - xDiff, p2.second + yDiff)
   } else if (p2.first > p1.first && p2.second > p1.second) {
      // diag right down
      return Pair<Int, Int>(p2.first + xDiff, p2.second + yDiff)
   } else if (p2.first < p1.first && p2.second < p1.second) {
      // diag left up
      return Pair<Int, Int>(p2.first - xDiff, p2.second - yDiff)
   } else {
      // diag right up
      return Pair<Int, Int>(p2.first + xDiff, p2.second - yDiff)
   }
}