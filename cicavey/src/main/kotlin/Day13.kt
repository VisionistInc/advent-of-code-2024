import java.io.File

data class Machine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long) {
    // This is cramer's rule. I found this after researching how to solve equations. I guess guassian elim works too?
    // seemed harder to implement. This was a top hit from google on solving 2 linear equations in Java/kotlin
    val det = (ax * by - ay * bx).toDouble()
    val x = (px * by - py * bx) / det;
    val y = (ax * py - ay * px) / det;

    val score =
        if(x % 1.0 == 0.0 && y % 1.0 == 0.0) {
            (3 * x + y).toLong()
        } else {
            0L
        }
}

val buttonRE = ".*?X\\+(\\d+), Y\\+(\\d+)".toRegex()
val prizeRE = ".*?X=(\\d+), Y=(\\d+)".toRegex()

fun parse(l : List<String>): Machine {
    val m = buttonRE.matchEntire(l[0])!!
    val ax = m.groups[1]!!.value.toLong()
    val ay = m.groups[2]!!.value.toLong()

    val m2 = buttonRE.matchEntire(l[1])!!
    val bx = m2.groups[1]!!.value.toLong()
    val by = m2.groups[2]!!.value.toLong()

    val m3 = prizeRE.matchEntire(l[2])!!
    val px = m3.groups[1]!!.value.toLong()
    val py = m3.groups[2]!!.value.toLong()

    return Machine(ax, ay, bx, by, px, py)
}

fun main() {
    // Remove blank lines so we can iterate in chunks of 3
    val l = File("src/main/kotlin/Day13.txt").readLines().filter { !it.isBlank() }

    println(l.chunked(3).sumOf { // ftw
        val machine = parse(it)
        machine.score
    })

    val offset = 10000000000000L
    println(l.chunked(3).sumOf {
        val machine = parse(it)
        machine.copy(px = machine.px + offset, py = machine.py + offset).score
    })
}