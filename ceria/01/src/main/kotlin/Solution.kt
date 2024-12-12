import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()
   var locs1 = mutableListOf<Int>()
   var locs2 = mutableListOf<Int>()
   for (l in lines) {
      val lists = l.split("\\s+".toRegex())
      locs1.add(lists[0].toInt())
      locs2.add(lists[1].toInt())
   }

   println("Solution 1: ${solution1(locs1, locs2)}")
   println("Solution 2: ${solution2(locs1, locs2)}")
}

private fun solution1(l1: List<Int>, l2: List<Int>): Int {
   var distance = 0 
   l1.sorted().zip(l2.sorted()).forEach { (i1, i2) -> 
      distance += abs(i2 - i1)
   }

   return distance
}

private fun solution2(l1: List<Int>, l2: List<Int>): Int {
   var distance = 0
   l1.forEach { l -> 
      distance += l2.count { it == l } * l
   }

   return distance
}
