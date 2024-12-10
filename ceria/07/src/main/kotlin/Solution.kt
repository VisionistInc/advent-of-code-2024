import java.io.File

import aoc.utils.math.stringMatrixCombinations

// in case the keys aren't unique, don't use a map
val testOps = mutableListOf<Pair<Long, List<Long>>>()
val ops = listOf<String>("+", "*")
val part2Ops = listOf<String>("+", "*", "||")

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()
   for (line in lines) {
      var parts = line.split(": ")
      var ops = parts[1].split(" ").map { it.toLong() }
      testOps.add(Pair(parts[0].toLong(), ops))
   }

   println("Solution 1: ${solution1()}")
   println("Solution 2: ${solution2()}")
}

private fun solution1(): Long {
   var workingTests = doAlgo(ops, testOps)
   return workingTests.map { it.first }.sum()
}

private fun solution2(): Long {
   var workingTests = doAlgo(part2Ops, testOps)
   return workingTests.map { it.first }.sum()
}

private fun doAlgo(
        ops: List<String>,
        testOps: List<Pair<Long, List<Long>>>
): MutableList<Pair<Long, List<Long>>> {
   var workingTests = mutableListOf<Pair<Long, List<Long>>>()
   for (opsPair in testOps) {

      var matrix = mutableListOf<List<String>>()
      for (item1 in opsPair.second) {
         var valsList = mutableListOf<String>()
         for (item2 in ops) {
            valsList.add("$item1:$item2 ")
         }
         matrix.add(valsList)
      }

      val combinations = stringMatrixCombinations(matrix)

      for (value in combinations) {
         if (doTest(opsPair.first, value.split(" ").filter{ it.isNotEmpty() })) {
            workingTests.add(opsPair)
            break
         }
      }
   }
   return workingTests
}

private fun doTest(expected: Long, equation: List<String>): Boolean {
   var solution = equation[0].split(":")[0].toLong()
   var prevOp = equation[0].split(":")[1]
   for (i in 0 until equation.size - 1) {
      val nextNum = equation[i + 1].split(":")[0].toLong()
      if (prevOp.equals("+")) {
         solution = solution + nextNum
      } else if (prevOp.equals("*")) {
         solution = solution * nextNum
      } else {
         solution = (solution.toString() + nextNum.toString()).toLong()
      }
      if (solution > expected) {
         break
      }

      // the last element in the list won't have an operator
      if (i + 1 < equation.size - 1) {
         prevOp = equation[i + 1].split(":")[1]
      }
   }

   if (solution == expected) {
      return true
   }

   return false
}

