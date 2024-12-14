import java.io.File;

import kotlin.math.abs

var width = 101
var height = 103
var xMid = width / 2
var yMid = height / 2

fun main(args : Array<String>) {
   val lines = File(args.first()).readLines()
   var robots = mutableMapOf<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>()

   var robot = 1
   lines.forEach{ line ->
      var parts = line.split(" ")
      var position = parts[0].split(",")
      var velocity = parts[1].split(",")
      var p = Pair(position[0].substring(position[0].indexOf("=") + 1).toInt(), position[1].toInt())
      var v = Pair(velocity[0].substring(velocity[0].indexOf("=") + 1).toInt(), velocity[1].toInt())
      robots.put(robot, Pair(p, v))
      robot++
   }

   var part2UnmutatedMap = robots.toMutableMap()
    println("Solution 1: ${solution1(robots)}")
    println("Solution 2: ${solution2(part2UnmutatedMap)}")
}

private fun solution1(robots: MutableMap<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>) :Int {
   var seconds = 0
   while ( seconds < 100) {
      moveRobots(robots)
      seconds++
   }

   var upLeftQuad = robots.filterValues { it.first.first < xMid && it.first.second < yMid}.entries.toList().size
   var downLeftQuad = robots.filterValues { it.first.first < xMid && it.first.second > yMid}.entries.toList().size
   var upRightQud = robots.filterValues { it.first.first > xMid && it.first.second < yMid}.entries.toList().size
   var downRightQud = robots.filterValues { it.first.first > xMid && it.first.second > yMid}.entries.toList().size

   return upLeftQuad * upRightQud * downLeftQuad * downRightQud
}

private fun solution2(robots: MutableMap<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>) :Int {
   var seconds = 0

   var upLeftQuad = robots.filterValues { it.first.first < xMid && it.first.second < yMid}.entries.toList().size
   var downLeftQuad = robots.filterValues { it.first.first < xMid && it.first.second > yMid}.entries.toList().size
   var upRightQud = robots.filterValues { it.first.first > xMid && it.first.second < yMid}.entries.toList().size
   var downRightQud = robots.filterValues { it.first.first > xMid && it.first.second > yMid}.entries.toList().size
   var safety = upLeftQuad * upRightQud * downLeftQuad * downRightQud
   var mark = 0

   while (true) {
      moveRobots(robots)
      seconds++
         
      upLeftQuad = robots.filterValues { it.first.first < xMid && it.first.second < yMid}.entries.toList().size
      downLeftQuad = robots.filterValues { it.first.first < xMid && it.first.second > yMid}.entries.toList().size
      upRightQud = robots.filterValues { it.first.first > xMid && it.first.second < yMid}.entries.toList().size
      downRightQud = robots.filterValues { it.first.first > xMid && it.first.second > yMid}.entries.toList().size

      var newSafety = upLeftQuad * upRightQud * downLeftQuad * downRightQud

      // HAd us calculate the safety score for a reason, so figure it's at either the lowewst
      // or the highest value or something related to the safety score
      if (newSafety < safety) {
         safety = newSafety
         mark = seconds
         printRobots(robots)
         println(seconds)
         repeat(5) { println() }
      }

      // if we haven't set a new mark in quadrouple the amount of time since we last saw
      // a new lowest safety, break and assume we have the picture. Start this check after
      // mark is 100 cause we know it doesn't happen within the first 100 seconds
      if (mark > 100 && seconds == mark * 4) {
         break
      }
   }

   return mark
}

private fun moveRobots(robots: MutableMap<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
   robots.forEach { (key, value) ->
      var currentPos = value.first
      var velocity = value.second
      var newPos = Pair(currentPos.first + velocity.first, currentPos.second + velocity.second)
      if (!inBounds(newPos)) {
         newPos = wrapPosition(newPos)
      }
      robots.put(key, Pair(newPos, velocity))
   }
}

private fun wrapPosition(pos: Pair<Int, Int>): Pair<Int, Int> {
   var wrappedPos = pos
   if (pos.first < 0) {
      // wrap to the right
      wrappedPos = Pair<Int, Int>(width - abs(pos.first), wrappedPos.second)
   } else if (pos.first >= width) {
      // wrap to the left
      wrappedPos = Pair<Int, Int>(pos.first - width, wrappedPos.second)
   }

   if (pos.second < 0) {
      // wrap to the bottom
      wrappedPos = Pair<Int, Int>(wrappedPos.first, height - abs(pos.second))
   } else if (pos.second >= height) {
      // wrap to the top
      wrappedPos = Pair<Int, Int>(wrappedPos.first, pos.second - height)
   }

   return wrappedPos
}

private fun inBounds(pos: Pair<Int, Int>): Boolean {
   return pos.first >= 0 && pos.second >= 0 && pos.first < width && pos.second < height
}

private fun printRobots(robots: Map<Int, Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
   for (y in 0 until height) {
      for (x in 0 until width) {
         var numRobots = robots.filterValues { it.first ==  Pair(x, y) }.entries.toList().size
      
         if (numRobots > 0) {
            print(numRobots)
         } else {
            print(".")
         }
      }
      println()
   }
}

