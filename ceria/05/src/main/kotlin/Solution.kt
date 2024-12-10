import java.io.File

val rulesAfter = mutableMapOf<Int, MutableList<Int>>()
val rulesBefore = mutableMapOf<Int, MutableList<Int>>()
var pages = mutableListOf<List<Int>>()

fun main(args: Array<String>) {
   val lines = File(args.first()).readLines()
   var beforeBlank = true
   for (l in lines) {

      if (l.isBlank()) {
         beforeBlank = false
         continue
      }

      if (beforeBlank) {
         var split = l.split("|").map { it.toInt() }
         if (rulesAfter.contains(split[0])) {
            rulesAfter.get(split[0])?.add(split[1])
         } else {
            var ruleList = mutableListOf(split[1])
            rulesAfter.put(split[0], ruleList)
         }
         if (rulesBefore.contains(split[1])) {
            rulesBefore.get(split[1])?.add(split[0])
         } else {
            var ruleList = mutableListOf(split[0])
            rulesBefore.put(split[1], ruleList)
         }
      } else {
         var order = l.split(",").map { it.toInt() }
         pages.add(order)
      }
   }

   println("Solution 1: ${solution1()}")
   println("Solution 2: ${solution2()}")
}

private fun solution1(): Int {
   val correctPages = getCorrectPages(pages)
   var sum = 0
   for (c in correctPages) {
      sum += findMiddleValue(c)
   }
   return sum
}

private fun solution2(): Int {
   val correctPages = getCorrectPages(pages)
   var incorrectPages = pages.filterNot { it in correctPages }
   var fixedPages = mutableListOf<List<Int>>()

   for (page in incorrectPages) {
      var fixedPage = fixPage(page.toMutableList())
      while (getCorrectPages(listOf(fixedPage)).isEmpty()) {
         fixedPage = fixPage(fixedPage)
      }
      fixedPages.add(fixedPage)
   }

   var sum = 0
   for (fixed in fixedPages) {
      sum += findMiddleValue(fixed)
   }
   return sum
}

private fun fixPage(page: MutableList<Int>): MutableList<Int> {
   var newPage = page
   for (i in page.indices) {
      var target = page[i]
      var before = listOf<Int>()
      if (i > 0) {
         before = page.subList(0, i)
      }

      if (!check(target, before, false)) {
         if (i != 0) {
            val temp = newPage[i]
            newPage[i] = newPage[i - 1]
            newPage[i - 1] = temp
         } else {
            val temp = newPage[i]
            newPage[i] = newPage[newPage.size - 1]
            newPage[newPage.size - 1] = temp
         }
         break
      }
   }
   return newPage
}

private fun getCorrectPages(pages: List<List<Int>>): List<List<Int>> {
   var correctPages = mutableListOf<List<Int>>()
   for (page in pages) {
      var correct = true
      for (i in page.indices) {
         var target = page[i]
         var before = listOf<Int>()
         if (i > 0) {
            before = page.subList(0, i)
         }

         var after = listOf<Int>()
         if (i != page.size) {
            after = page.subList(i + 1, page.size)
         }

         if (check(target, before, false) && check(target, after, true)) {
            continue
         } else {
            correct = false
            break
         }
      }

      if (correct) {
         correctPages.add(page)
      }
   }
   return correctPages
}

private fun check(pNum: Int, pages: List<Int>, after: Boolean): Boolean {
   if (pages.isEmpty()) {
      return true
   }
   if (after) {
      if (rulesAfter.contains(pNum)) {
         return rulesAfter.get(pNum)!!.containsAll(pages)
      }
   } else {
      if (rulesBefore.contains(pNum)) {
         return rulesBefore.get(pNum)!!.containsAll(pages)
      }
   }

   return false
}

private fun findMiddleValue(list: List<Int>): Int {
   val middleIndex = list.size / 2

   return if (list.size % 2 == 0) {
      // Even-sized list, return the average of the two middle elements
      (list[middleIndex - 1] + list[middleIndex]) / 2
   } else {
      // Odd-sized list, return the middle element
      list[middleIndex]
   }
}
