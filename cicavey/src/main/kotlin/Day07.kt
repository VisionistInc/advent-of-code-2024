import java.io.File

fun permuteop(result: Long, partial: Long, data: List<Long>): Boolean {
    if(data.isEmpty()) {
        return result == partial
    }
    val newData = data.subList(1, data.size)
    return permuteop(result, partial + data[0], newData) ||
            permuteop(result, partial * data[0], newData)
}

fun permuteop2(result: Long, partial: Long, data: List<Long>): Boolean {
    if(data.isEmpty()) {
        return result == partial
    }
    val newData = data.subList(1, data.size)
    return permuteop2(result, partial + data[0], newData) ||
            permuteop2(result, partial * data[0], newData) ||
            permuteop2(result, (partial.toString() + data[0].toString()).toLong(), newData)
}

fun main() {

    var p1 = 0L
    var p2 = 0L

    val calibrationResult = File("src/main/kotlin/Day07.txt").readLines().forEach {
        val s = it.split(": ")
        val result = s[0].toLong()
        val data = s[1].split("\\s+".toRegex()).map { it.toLong() }.toList()

        if(permuteop(result, partial = data[0], data.subList(1, data.size))) {
            p1 += result
        }
        if(permuteop2(result, partial = data[0], data.subList(1, data.size))) {
            p2 += result
        }
    }

    println(p1)
    println(p2)

}