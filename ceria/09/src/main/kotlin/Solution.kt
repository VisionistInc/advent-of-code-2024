import java.io.File
import idsToSize

var expanded = mutableListOf<Long>()
val idsToSize = mutableMapOf<Long, Int>()
val freeIdxToSize = mutableMapOf<Int, Int>()

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()

   var ID = 0L
   // var isFile = true
   lines[0].forEachIndexed { index, char ->
      if (index == 0 || index % 2 == 0) {
         var num = char.digitToInt()
         if (num > 0) {
            repeat(num) { expanded.add(ID) }
            idsToSize.put(ID, num)
            ID++
         }
         // isFile = false
      } else {
         var num = char.digitToInt()
         if (num > 0) {
            repeat(num) { expanded.add(-1L) }
         }
         freeIdxToSize.put(index, num)
         // isFile = true
      }
   }
   freeIdxToSize.toSortedMap()
   //  println("Solution 1: ${solution1(expanded)}")
   println("Solution 2: ${solution2(expanded)}")
}

private fun solution1(expanded: MutableList<Long>): Long {
   while (!isCompacted(expanded)) {
      var freeMemIdx = expanded.indexOf(-1L)
      var fileIdx = expanded.indexOfLast { it != -1L }
      expanded.swap(freeMemIdx, fileIdx)
   }
   var endFileIdx = expanded.indexOfLast { it != -1L }
   return checksum(expanded.subList(0, endFileIdx + 1))
}

private fun solution2(expanded: MutableList<Long>): Long {
   // println(expanded)
   var sortedIdsToSize = idsToSize.toSortedMap(reverseOrder())

   var skipIdx = expanded.size - 1
   for ((fileId, size) in sortedIdsToSize) {
      
      var lastIdIdx = expanded.indexOf(fileId)

      // look to the left, the sub list should be what's left of where we have processed
      var startFreeMemIdx = expanded.subList(0, skipIdx).findFirstWindowWithSameElements(size)
      // println("file is  ${fileId}    size is ${size}    skipIdx is ${skipIdx}  startFreeMemIdx is ${startFreeMemIdx}")

      if (startFreeMemIdx != null) {
         var endFreeMemIdx = startFreeMemIdx + size
         if (endFreeMemIdx == lastIdIdx) {
            println("#########################################333")
         }
         // if (endFreeMemIdx == lastIdIdx) {
         //    // The empty space and file are right next to each other, and both are the same size
         //    // This was my issue because swap does not handle
         //    println("==============================================")
         //    println("free start: ${startFreeMemIdx}    free end: ${endFreeMemIdx}")
         //    println("file start index:  ${lastIdIdx}")
         //    println("Value actually at the index in question   ${expanded[49279]}")
         //    println(endFreeMemIdx - startFreeMemIdx)
         //    println(size)
         //    println("==============================================")
           
         // } else {

         for (i in startFreeMemIdx until endFreeMemIdx) {
            expanded.swap(i, lastIdIdx)
            lastIdIdx++
         }
      // }
         skipIdx = lastIdIdx - 1
      } else {
         // no space big enough to the left to move this file
         skipIdx = skipIdx - size
      }

      // println(expanded)
   }

   return checksum(expanded)
}

fun <Long> List<Long>.findFirstWindowWithSameElements(windowSize: Int): Int? {
   if (windowSize <= 0) return null
   if (size < windowSize) return null

   for (i in 0..size - windowSize) {
      val window = subList(i, i + windowSize)
      if (window.all { it == -1L }) {
         return i
      }
   }
   return null
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
   val temp = this[index1]
   this[index1] = this[index2]
   this[index2] = temp
}

private fun isCompacted(expanded: MutableList<Long>): Boolean {
   var lastFileIdx = expanded.indexOfLast { it != -1L }
   return expanded.subList(0, lastFileIdx + 1).count { it == -1L } == 0
}

private fun checksum(expanded: List<Long>): Long {
   var checksum = 0L
   for (i in expanded.indices) {
      if (expanded[i] != -1L) {
         checksum += expanded[i] * i
      }
   }
   return checksum
}
