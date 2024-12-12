package aoc.utils.math

fun gcd(n1: Int, n2: Int): Int {
    if (n2 == 0) {
        return n1
    }
    return gcd(n2, n1 % n2)
}

fun lcm(n1: Int, n2: Int): Int  = (n1 * n2) / gcd(n1, n2)

fun stringMatrixCombinations(matrix: List<List<String>>): List<String> {
    val result = mutableListOf<String>()
    combine(matrix, 0, "", result)
    return result
 }
 
 private fun combine(matrix: List<List<String>>, row: Int, current: String, result: MutableList<String>) {
    if (row == matrix.size) {
       result.add(current)
       return
    }
 
    for (element in matrix[row]) {
       combine(matrix, row + 1, current + element, result)
    }
 }
