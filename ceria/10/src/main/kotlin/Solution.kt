import java.io.File;

var xLimits = 0
var yLimits = 0

fun main(args : Array<String>) {
    val lines = File(args.first()).readLines()
    var trails = Array<Array<Int>>(lines.size) { Array<Int>(lines[0].length ) { 0 } }

    lines.forEachIndexed { y, line ->
      line.forEachIndexed { x, value ->
         trails[y][x] = value.digitToInt()
      }
   }
   xLimits = lines[0].length
   yLimits = lines.size

    println("Solution 1: ${solution1(trails)}")
    println("Solution 2: ${solution2(trails)}")
}

private fun solution1(trails: Array<Array<Int>>) :Int {
  var goodTrails = 0
  for ((rowNum, row) in trails.withIndex()) {
      var trailHeads = row.indices.filter { row[it] == 0 }.map{ Pair<Int, Int>(it, rowNum) }
      for (trailHead in trailHeads) {
         var tSet = findUniqueTrailsEndingAt9(trailHead, trails, 0)
         var goodCount = 0
         for (t in tSet) {
            if (trails[t.second][t.first] == 9) {
               goodCount++
            }
         }
         goodTrails += goodCount
      }
  }

  return goodTrails
}

private fun solution2(trails: Array<Array<Int>>) :Int {
   var goodTrails = 0
  for ((rowNum, row) in trails.withIndex()) {
      var trailHeads = row.indices.filter { row[it] == 0 }.map{ Pair<Int, Int>(it, rowNum) }
      for (trailHead in trailHeads) {
         var tList = findTrailsEndingAt9(trailHead, trails, 0).filter{ it == 9 }
         goodTrails += tList.size
      }
  }

  return goodTrails
}

private fun findUniqueTrailsEndingAt9(startFrom: Pair<Int, Int>, trails: Array<Array<Int>>, currentValue: Int): Set<Pair<Int, Int>> {
   var endValues = mutableSetOf<Pair<Int, Int>>()

   // find neighbors
   var up = Pair<Int, Int>(startFrom.first, startFrom.second - 1)
   var down = Pair<Int, Int>(startFrom.first, startFrom.second + 1)
   var left = Pair<Int, Int>(startFrom.first - 1, startFrom.second)
   var right = Pair<Int, Int>(startFrom.first + 1, startFrom.second)

   // if the neighbor is in bounds, (exists) and is greater than the current value, keep finding
   if (exists(up) && trails[up.second][up.first] == currentValue + 1) {
      endValues.add(Pair<Int, Int>(up.first, up.second))
      endValues.addAll(findUniqueTrailsEndingAt9(up, trails, currentValue + 1))
   }
   if (exists(down) && trails[down.second][down.first] == currentValue + 1) {
      endValues.add(Pair<Int, Int>(down.first, down.second))
      endValues.addAll(findUniqueTrailsEndingAt9(down, trails, currentValue + 1))
   }
   if (exists(left) && trails[left.second][left.first] == currentValue + 1) {
      endValues.add(Pair<Int, Int>(left.first, left.second))
      endValues.addAll(findUniqueTrailsEndingAt9(left, trails, currentValue + 1))
   }
   if (exists(right) && trails[right.second][right.first] == currentValue + 1) {
      endValues.add(Pair<Int, Int>(right.first, right.second))
      endValues.addAll(findUniqueTrailsEndingAt9(right, trails, currentValue + 1))
   }

   return endValues
}

private fun findTrailsEndingAt9(startFrom: Pair<Int, Int>,  trails: Array<Array<Int>>, currentValue: Int): List<Int> {
   var endValues = mutableListOf<Int>()

   // find neighbors
   var up = Pair<Int, Int>(startFrom.first, startFrom.second - 1)
   var down = Pair<Int, Int>(startFrom.first, startFrom.second + 1)
   var left = Pair<Int, Int>(startFrom.first - 1, startFrom.second)
   var right = Pair<Int, Int>(startFrom.first + 1, startFrom.second)

   // if the neighbor is in bounds, (exists) and is greater than the current value, keep finding
   if (exists(up) && trails[up.second][up.first] == currentValue + 1) {
      endValues.add(trails[up.second][up.first])
      endValues.addAll(findTrailsEndingAt9(up, trails, currentValue + 1))
   }
   if (exists(down) && trails[down.second][down.first] == currentValue + 1) {
      endValues.add(trails[down.second][down.first])
      endValues.addAll(findTrailsEndingAt9(down, trails, currentValue + 1))
   }
   if (exists(left) && trails[left.second][left.first] == currentValue + 1) {
      endValues.add(trails[left.second][left.first])
      endValues.addAll(findTrailsEndingAt9(left, trails, currentValue + 1))
   }
   if (exists(right) && trails[right.second][right.first] == currentValue + 1) {
      endValues.add(trails[right.second][right.first])
      endValues.addAll(findTrailsEndingAt9(right, trails, currentValue + 1))
   }

   return endValues
}

private fun exists(point: Pair<Int, Int>): Boolean {
   return point.first != -1 && point.second != -1 && point.first < xLimits && point.second < yLimits
}