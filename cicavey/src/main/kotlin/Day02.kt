import java.io.File
import java.lang.Integer.signum
import kotlin.collections.windowed
import kotlin.math.abs

private fun safe(l : List<Int>): Boolean {
    var direction = signum(l[1] - l[0])
    return l.windowed(2).all { window ->
        val delta = window[1] - window[0]
        val newDirection = signum(delta)
        newDirection == direction && abs(delta) in 1..3
    }
}

fun main() {
    val reports = File("src/main/kotlin/Day02.txt").readLines().map {
        it.split("\\s+".toRegex()).map { it.toInt() }.toList()
    }

    val safeReports = reports.count(::safe)

    println(safeReports)

    val safeDampedReports = reports.count {
        // Try versions of the list, skipping an index. Inefficient copies. /shrug
        safe(it) || it.indices.any { idx ->
            safe(it.filterIndexed { index, i -> index != idx })
        }
    }

    println(safeDampedReports)
}