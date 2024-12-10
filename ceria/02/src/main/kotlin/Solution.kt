import java.io.File

import kotlin.math.abs

val valid = 1..3
fun main(args : Array<String>) {
    val reports = File(args.first()).readLines()

    println("Solution 1: ${solution1(reports)}")
    println("Solution 2: ${solution2(reports)}")
}

private fun solution1(input: List<String>) :Int {
   var safeReports = mutableListOf<String>()
   for (report in input) {
      if (isSafe(report.split(" ").map{ it.toInt() })) {
         safeReports.add(report)
      }
   }
   return safeReports.size
}

private fun solution2(input:  List<String>) :Int {
   var safeReports = mutableListOf<String>()
   for (report in input) {
      var levels = report.split(" ").map{ it.toInt() }
      if (isSafe(levels)) {
         safeReports.add(report)
      } else {
         for (i in levels.indices) {
            var copy = levels.toMutableList()  // wasteful, but I'm lazy
            copy.removeAt(i)
            if (isSafe(copy)) {
               safeReports.add(report)
               break
            }
         }
      }
   }
   return safeReports.size
}

private fun isSafe(levels: List<Int>): Boolean {
   var safe = true
   var increasing = levels[1] - levels[0] > 0
   for (i in 1 until levels.size) {
      val diff = levels[i] - levels[i - 1]
      if ( !(increasing && diff > 0 && diff in valid)  &&
         !(!increasing && diff < 0 && abs(diff) in valid) ) {
            safe = false
            break
      }
   }
   return safe
}

