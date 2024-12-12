import java.io.File
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.text.Regex

val mulPattern = Regex("mul\\(\\d+,\\d+\\)")
val doPattern = Regex("do\\(\\)")
val dontPattern = Regex("don't\\(\\)")

fun main(args: Array<String>) {
   val input = File(args.first()).readLines()

   println("Solution 1: ${solution1(input)}")
   println("Solution 2: ${solution2(input)}")
}

private fun solution1(input: List<String>): Int {
   var matches = mulPattern.findAll(input.joinToString(separator = ""))
   var sum = mutableListOf<Int>()
   val pattern = Pattern.compile("\\d+")
   for (match in matches) {
      val numbers = mutableListOf<Int>()
      val matcher = pattern.matcher(match.value)
      while (matcher.find()) {
         numbers.add(matcher.group().toInt())
      }
      sum.add(numbers.reduce { acc, num -> acc * num })
   }

   return sum.sum()
}

private fun solution2(input: List<String>): Int {
   val mulMatches = mulPattern.findAll(input.joinToString(separator = ""))
   var doIndexes =
           doPattern
                   .findAll(input.joinToString(separator = ""))
                   .map { it.range.first }
                   .toMutableList()
   var dontIndexes =
           dontPattern
                   .findAll(input.joinToString(separator = ""))
                   .map { it.range.first }
                   .toMutableList()

   var sum = mutableListOf<Int>()
   val pattern = Pattern.compile("\\d+")
   var doMul = true
   for (mulMatch in mulMatches) {
      if (mulMatch.range.first < doIndexes.first() && mulMatch.range.first < dontIndexes.first()) {
         // no op
      } else {
         var doClosest = findClosestNumber(doIndexes, mulMatch.range.first)
         var dontClosest = findClosestNumber(dontIndexes, mulMatch.range.first)
         if (abs(doClosest - mulMatch.range.first) < abs(dontClosest - mulMatch.range.first)) {
            doMul = true
         } else {
            doMul = false
         }
      }

      if (doMul) {
         val numbers = mutableListOf<Int>()
         val matcher = pattern.matcher(mulMatch.value)
         while (matcher.find()) {
            numbers.add(matcher.group().toInt())
         }
         sum.add(numbers.reduce { acc, num -> acc * num })
      }
   }

   return sum.sum()
}

private fun findClosestNumber(list: List<Int>, target: Int): Int {
   if (target < list[0]) {
      return 0
   }

   for ((idx, num) in list.withIndex()) {
      if (num > target) {
         if (idx > 0) {
            return list[idx - 1]
         } else {
            return list[0]
         }
      }
   }

   return list.last()
}
