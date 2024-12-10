import java.io.File

val obstacles = mutableListOf<Pair<Int, Int>>()
var start = Pair<Int, Int>(0, 0)

lateinit var xBounds: IntRange
lateinit var yBounds: IntRange

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()
   lines.forEachIndexed { y, line ->
      line.forEachIndexed { x, value ->
         when (value) {
            '#' -> obstacles.add(Pair<Int, Int>(x, y))
            '^' -> start = Pair<Int, Int>(x, y)
         }
      }
   }
   xBounds = 0..lines[0].length
   yBounds = 0..lines.size - 1

   var guardPos = start
   println("Solution 1: ${solution1(guardPos)}")
   println("Solution 2: ${solution2(guardPos)}")
}

private fun solution1(gPos: Pair<Int, Int>): Int {
   var guardPos = gPos
   var positions = mutableListOf<Pair<Int, Int>>()
   var direction = 'u'

   while (guardPos.first in xBounds && guardPos.second in yBounds) {
      positions.add(guardPos)
      var movement = move(guardPos, direction)
      guardPos = movement.first
      direction = movement.second
   }

   return positions.distinctBy { it }.size
}

// 467 = too low
private fun solution2(gPos: Pair<Int, Int>): Int {
   var guardPos = gPos
   var positions = mutableListOf<Pair<Int, Int>>()
   var directions = mutableListOf<Char>()
   var direction = 'u'
   directions.add(direction)

   while (guardPos.first in xBounds && guardPos.second in yBounds) {
      positions.add(guardPos)
      var movement = move(guardPos, direction)
      guardPos = movement.first
      direction = movement.second
      directions.add(direction)
   }
   var loops = mutableSetOf<Pair<Int, Int>>()

   // for (obstacleIdx in 1..positions.size - 1) {
   //    // never put an obstacle at the start
   //    if (positions[obstacleIdx] == start) {
   //       continue
   //    }

   //    for (startIdx in 0..obstacleIdx) {
   //       var potentialLoop = positions.subList(startIdx, obstacleIdx )
   //       // println(potentialLoop)
   //       if (detectLoop(potentialLoop)) {
   //          loops.add()
   //          println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
   //          // println(potentialLoop)
   //       }
   //    }
   // }

   // positions = positions.distinctBy { it }.toMutableList()

   // go through and put an obstacle at each position. If doing so
   // causes a loop. add it to the loopPaths

   //
   outter@ for (obsIdx in 1..positions.size - 1) {
      // Never put an obstacle at the start position
      if (positions[obsIdx] == start) {
         continue
      }

      var pos = positions[obsIdx]
      obstacles.add(pos)

      for (startIdx in 0..obsIdx) {
         guardPos = positions[startIdx] // reset the guard
         direction = directions[startIdx] // reset the direction
         var loopCandidate = mutableListOf<Pair<Int, Int>>()

         while (guardPos.first in xBounds && guardPos.second in yBounds) {
            var movement = moveUntil(guardPos, direction, pos)
            guardPos = movement.first.first
            direction = movement.first.second
            // currIdx++
            // println("movement: " + movement)
            if (movement.second) {
               println("movement pos is " + movement.first.first)
               loopCandidate.add(movement.first.first)
            }
            if (loopCandidate.size > 1) {
               loopCandidate.add(movement.first.first)

               if (loopCandidate.size > 4) {
                  // var potentialLoop = positions.subList(startIdx, obsIdx)
                  // potentialLoop.add(guardPos)
                  // println(potentialLoop)
                  if (detectLoop(loopCandidate)) {
                     println("pos:  " + pos + "            loop  " + loopCandidate)
                     loops.add(pos)
                     break
                     // break@outter
                     // loops.add(potentialLoop)
                  }
               }
            }
         }
      }

      if (pos != start) {
         obstacles.remove(pos)
      }
   }
   println(loops)
   // println(detectLoop.distinctBy { it })
   // return positions.size - positions.distinctBy { it }.size
   return -1
}

private fun forcedTurn(oldDir: Char, newDir: Char): Boolean {
   var forcedTurn = false
   when (oldDir) {
      'u' -> forcedTurn = (newDir == 'l' || newDir == 'r')
      'd' -> forcedTurn = (newDir == 'l' || newDir == 'r')
      'l' -> forcedTurn = (newDir == 'u' || newDir == 'd')
      'r' -> forcedTurn = (newDir == 'u' || newDir == 'd')
   }
   return forcedTurn
}

private fun moveUntil(
        pos: Pair<Int, Int>,
        dir: Char,
        obsPos: Pair<Int, Int>
): Pair<Pair<Pair<Int, Int>, Char>, Boolean> {
   // find the point that would be moved to
   var direction = dir
   var oldPos = pos
   var newPos = getNewPos(oldPos, direction)

   if (newPos == obsPos) {
      // woud be moving to the new obstacle position. so move, expecting to enounter
      // the obstacle, but noting it

      var obsMoveTo = move(pos, dir)
      // need to return what the next move's direction would be
      // var nextMove = move(obsMoveTo.first, obsMoveTo.second)
      return Pair<Pair<Pair<Int, Int>, Char>, Boolean>(Pair(obsMoveTo.first, direction), true)
   } else {
      return Pair<Pair<Pair<Int, Int>, Char>, Boolean>(move(pos, dir), false)
   }
}

private fun move(pos: Pair<Int, Int>, dir: Char): Pair<Pair<Int, Int>, Char> {
   // find the point that would be moved to
   var direction = dir
   var oldPos = pos
   var newPos = getNewPos(oldPos, direction)

   // check for an obstacle at that new position
   while (obstacles.contains(newPos)) {
      // change the direction and get a new position
      direction = turn(direction)
      newPos = getNewPos(oldPos, direction)
   }

   return Pair<Pair<Int, Int>, Char>(newPos, direction)
}

private fun turn(dir: Char): Char {
   var direction = dir
   when (dir) {
      'u' -> direction = 'r'
      'r' -> direction = 'd'
      'd' -> direction = 'l'
      'l' -> direction = 'u'
   }
   return direction
}

private fun getNewPos(pos: Pair<Int, Int>, dir: Char): Pair<Int, Int> {
   var newPos = pos
   when (dir) {
      'u' -> newPos = Pair(pos.first, pos.second - 1)
      'd' -> newPos = Pair(pos.first, pos.second + 1)
      'l' -> newPos = Pair(pos.first - 1, pos.second)
      'r' -> newPos = Pair(pos.first + 1, pos.second)
   }
   return newPos
}

fun <T> List<T>.containsSublist(sublist: List<T>): Boolean {
   if (sublist.isEmpty()) return true // An empty list is always a sublist
   if (sublist.size > this.size) return false

   for (i in 0..this.size - sublist.size) {
      if (this.subList(i, i + sublist.size) == sublist) {
         return true
      }
   }
   return false
}

private fun <T> List<T>.containsAllInOrder(other: List<T>): Boolean {
   if (other.isEmpty()) return true // An empty list is always a sublist
   if (size < other.size) return false

   var startIndex = 0
   while (startIndex + other.size <= size) {
      if (subList(startIndex, startIndex + other.size) == other) {
         return true
      }
      startIndex++
   }
   return false
}

data class Point(val x: Int, val y: Int)

fun detectLoop(coordinates: List<Pair<Int, Int>>): Boolean {
   val visited = mutableSetOf<Pair<Int, Int>>()

   for (coord in coordinates) {
      if (coord in visited) {
         return true // Loop detected
      }
      visited.add(coord)
   }

   return false // No loop found
}
