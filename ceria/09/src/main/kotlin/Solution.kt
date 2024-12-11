import java.io.File

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()

   println("Solution 1: ${solution1(lines)}")
   println("Solution 2: ${solution2(lines)}")
}

private fun solution1(lines: List<String>): Long {
   var expanded = mutableListOf<Long>()
   val idsToSize = mutableMapOf<Long, Int>()

   var ID = 0L
   lines[0].forEachIndexed { index, char ->
      if (index == 0 || index % 2 == 0) {
         var num = char.digitToInt()
         if (num > 0) {
            repeat(num) { expanded.add(ID) }
            idsToSize.put(ID, num)
            ID++
         }
      } else {
         var num = char.digitToInt()
         if (num > 0) {
            repeat(num) { expanded.add(-1L) }
         }
      }
   }

   while (!isCompacted(expanded)) {
      var freeMemIdx = expanded.indexOf(-1L)
      var fileIdx = expanded.indexOfLast { it != -1L }
      expanded.swap(freeMemIdx, fileIdx)
   }
   var endFileIdx = expanded.indexOfLast { it != -1L }
   return checksum(expanded.subList(0, endFileIdx + 1))
}

private fun solution2(lines: List<String>): Long {
   var expanded = mutableListOf<Long>()
   val idsToSize = mutableMapOf<Long, Int>()

   var ID = 0L
   lines[0].forEachIndexed { index, char ->
      if (index == 0 || index % 2 == 0) {
         var num = char.digitToInt()
         if (num > 0) {
            repeat(num) { expanded.add(ID) }
            idsToSize.put(ID, num)
            ID++
         }
      } else {
         var num = char.digitToInt()
         if (num > 0) {
            repeat(num) { expanded.add(-1L) }
         }
      }
   }

   var sortedIdsToSize = idsToSize.toSortedMap(reverseOrder())
   var skipIdx = expanded.size - 1
   for ((fileId, size) in sortedIdsToSize) {
      var lastIdIdx = expanded.indexOf(fileId)

      // look to the left, the sub list should be what's left of where we have processed
      var startFreeMemIdx = expanded.subList(0, skipIdx + 1).findFirstWindowWithSameElements(size)

      if (startFreeMemIdx != null) {
         var endFreeMemIdx = startFreeMemIdx + size

         for (i in startFreeMemIdx until endFreeMemIdx) {
            expanded.swap(i, lastIdIdx)
            lastIdIdx++
         }
         // THIS IS THE TRICK
         // Have to subtract 2, 1 to account for size being one mre than the same index space
         // (i.e. a size of 4 fits in indexes 82 - 85), but also to put the pointer just before
         // where the file began before it was moved, i.e don't process the same index twice.
         skipIdx = lastIdIdx - size - 2
      } else {
         // no space big enough to the left to move this file
         skipIdx = skipIdx - size - 1
      }
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

fun <Long> MutableList<Long>.swap(index1: Int, index2: Int) {
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
