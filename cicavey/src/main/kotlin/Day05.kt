import java.io.File

class MultiMap<K, T>: HashMap<K, MutableList<T>>() {
    fun add(key: K, value: T) {
        computeIfAbsent(key) { mutableListOf() }.add(value)
    }
    fun all(key: K): MutableList<T> = computeIfAbsent(key) { mutableListOf() }
}

fun isSorted(array: List<Int>): Boolean {
    for (i in 0 until array.size - 1) {
        if (array[i] > array[i + 1]) {
            return false
        }
    }
    return true
}

fun main() {
    val lines = File("src/main/kotlin/Day05.txt").readLines(Charsets.UTF_8)
    var mode = 0
    val constraints = mutableListOf<Pair<Int, Int>>()
    var correctMiddleSum = 0
    var fixedMiddleSum = 0
    lines.forEach { line ->
        if (line.isEmpty()) {
            mode = 1
            return@forEach
        }
        if(mode == 0) {
            val p = line.split("|").map { it.toInt() }.toIntArray()
            constraints.add(Pair(p[0], p[1]))
        } else {
            val pages = line.split(",").map { it.toInt() }.toIntArray()

            // Find all the constraints that involve _this_ set of pages
            val res = MultiMap<Int, Int>()
            constraints.filter { pages.contains(it.first) && pages.contains(it.second) }.forEach {
                res.add(it.second, it.first)
            }

            val weights = pages.map { res.all(it).size }.toList()
            if(isSorted(weights)) {
                correctMiddleSum += pages[pages.size/2]
            } else {
                val fixedPages = pages.sortedBy { res.all(it).size }
                fixedMiddleSum += fixedPages[fixedPages.size/2]
            }
        }
    }
    println(correctMiddleSum)
    println(fixedMiddleSum)
}