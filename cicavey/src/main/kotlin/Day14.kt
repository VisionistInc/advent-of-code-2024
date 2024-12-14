import java.io.File
import kotlin.system.exitProcess


data class Robot(var p: Point2, var v: Vec2) {
    fun tick() {
        p += v
    }
    fun wrap(w: Long, h: Long) {
        p = Point2(((p.x % w) + w) % w, ((p.y % h) + h) % h)
    }
}

val robotRE = "p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)".toRegex()

fun main() {

//    val w = 11L
//    val h = 7L

    val w = 101L
    val h = 103L

    // Remove blank lines so we can iterate in chunks of 3
    val robots = File("src/main/kotlin/Day14.txt").readLines().map {
        val m = robotRE.matchEntire(it)!!
        val px = m.groups[1]!!.value.toInt()
        val py = m.groups[2]!!.value.toInt()
        val vx = m.groups[3]!!.value.toInt()
        val vy = m.groups[4]!!.value.toInt()
        Robot(Point2(px, py), Vec2(vx, vy))
    }.toList()

    val r1 = robots.map { it.copy() }

    val max = 100
    (0..<max).forEach { step ->
        r1.forEach { robot ->
            robot.tick()
            robot.wrap(w, h)
        }
//        val g = SparseGrid(".")
//        r1.forEach { robot -> g.set(robot.p, "#") }
//        val s = g.toBoundedString()
//        if("##########" in s) {
//            print("$step\n$s\n")
//        }
    }

    val hist = mutableMapOf<Point2, Int>()
    r1.forEach { robot ->
        hist[robot.p] = hist.getOrDefault(robot.p, 0) + 1
    }

    println(hist.map { (pt, count) -> pt.q(w, h) to count }.filter { it.first != 0 }.groupBy { it.first }.map { qit ->
        qit.key to qit.value.sumOf { it.second }
    }.map { it.second }.reduce { acc, num -> acc * num })

    // part 2
    val r2 = robots.map { it.copy() }
    (0..<10000).forEach { step ->
        r2.forEach { robot ->
            robot.tick()
            robot.wrap(w, h)
        }
        // janky
        val g = SparseGrid(".")
        r2.forEach { robot -> g.set(robot.p, "#") }
        val s = g.toBoundedString()
        if("##########" in s) {
            print("${step+1}\n$s\n")
            exitProcess(0)
        }
    }
}