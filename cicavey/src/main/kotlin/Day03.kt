import java.io.File
import java.lang.Integer.signum
import kotlin.collections.windowed
import kotlin.math.abs

fun main() {

    val memory = File("src/main/kotlin/Day03.txt").readText()

    val sum1 = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex().findAll(memory).sumOf {
        val a = it.groupValues[1].toInt()
        val b = it.groupValues[2].toInt()
        a*b
    }
    println(sum1)

    var en = true
    val sum2 = """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex().findAll(memory).sumOf {
        if(it.value == "do()") {
            en = true
            0
        } else if(it.value == "don't()") {
            en = false
            0
        } else {
            if(en) {
                val a = it.groupValues[1].toInt()
                val b = it.groupValues[2].toInt()
                a*b
            } else {
                0
            }
        }
    }
    println(sum2)
}