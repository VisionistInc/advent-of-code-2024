package aoc.utils.parse

import java.io.File

fun toCharGrid(input: File): MutableList<List<Char>> {
    var grid = mutableListOf<List<Char>>()
    input.readLines().forEach {
        var row = it.toList()
        grid.add(row)
    }
    return grid
}

fun toIntGrid(input: File): MutableList<List<Int>> {
    var grid = mutableListOf<List<Int>>()
    input.readLines().forEach {
        var row = it.toList().map { it.digitToInt() }
        grid.add(row)
    }
    return grid
}
