import java.io.File


// shared cache across invocations
val cache = mutableMapOf<Pair<Long, Long>, Long>()

fun blink(level: Long, v: Long): Long {

    if(level == 0L) {
        return 1L
    }

    return cache.getOrPut(v to level) {
        if (v == 0L) {
            blink(level - 1, 1L)
        } else if (v.toString().length % 2 == 0) {
            val s = v.toString()
            val h = s.length / 2
            val a = s.substring(0, h).toLong()
            val b = s.substring(h).toLong()
            blink(level - 1, a) + blink(level - 1, b)
        } else {
            blink(level - 1, v * 2024)
        }
    }
}

fun driver(blinks: Int, l: List<Long>): Long {
    var r = 0L
    (0..blinks).forEach { b ->
        r = l.sumOf { blink(b.toLong(), it) }
    }
    return r
}

fun main() {
    var l = File("src/main/kotlin/Day11.txt").readText().split("\\s+".toRegex()).map { it.toLong() }.toList()
    println(driver(25, l))
    println(driver(75, l))
}