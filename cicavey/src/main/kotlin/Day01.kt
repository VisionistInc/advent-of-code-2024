import java.io.File
import kotlin.math.abs

fun main() {
    val a = mutableListOf<Int>()
    val b = mutableListOf<Int>()

    File("src/main/kotlin/Day01.txt").readLines().map {
        val r = it.split("\\s+".toRegex())
        a += r[0].toInt()
        b += r[1].toInt()
    }

    a.sort()
    b.sort()

    val sum = a.zip(b).sumOf { (a, b) -> abs(a - b) }

    println(sum)

    val similarity = a.sumOf { left -> left * b.count { left == it } }

    println(similarity)
}
