package aoc.utils.graph

import java.util.PriorityQueue
import java.util.Stack

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun euclideanDistance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Double =
    sqrt( (p2.first - p1.first).toDouble().pow(2) +  (p2.second -p1.second).toDouble().pow(2))

fun manhattanDistance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int =
    abs(p1.first - p2.first) + abs(p1.second - p2.second)

fun checkAreInLine(p1: Pair<Int, Int>, p2: Pair<Int, Int>, p3: Pair<Int, Int>): Boolean {
    // Using the slope method
    val slope1 = (p2.second - p1.second) / (p2.first - p1.first)
    val slope2 = (p3.second - p2.second) / (p3.first - p2.first)
     
    return slope1 == slope2
}
     
fun checkEqualsDistance(p1: Pair<Int, Int>, p2: Pair<Int, Int>, p3: Pair<Int, Int>): Boolean {
    var p1Top3 = manhattanDistance(p1, p3)
    var p2Top3 = manhattanDistance(p2, p3)
    if (p1Top3 > p2Top3) {
        return p1Top3 / p2Top3 == 2
    }
    return p2Top3 / p1Top3 == 2
 }

/**
 * Breath first search
 */
fun bfs(graph: Map<Pair<Int, Int>, List<Pair<Int, Int>>>, start: Pair<Int, Int>): Set<Pair<Int, Int>> {
    // keep track of the nodes we have visited
    val visited = mutableSetOf<Pair<Int, Int>>()
    // keep track of the nodes we have NOT visited
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(start)  // start by saying we haven't visited start so that we don't have to make a special case to handle it
    
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        if (node !in visited) {
            visited.add(node) // we are currently doing the visiting...
            graph[node]?.let { neighbors -> queue.addAll(neighbors.filterNot { it in visited }) }
        }
    }
    return visited
}

/**
 * Depth first search
 */
fun dfs(graph: Map<Pair<Int, Int>, List<Pair<Int, Int>>>, start: Pair<Int, Int>): Set<Pair<Int, Int>>  {
    // keep track of the nodes we have visited
    val visited = mutableSetOf<Pair<Int, Int>>()
    // keep track of the nodes we have NOT visited
    val stack = Stack<Pair<Int, Int>>()
    stack.push(start)  // start by saying we haven't visited start so that we don't have to make a special case to handle it

    while (stack.isNotEmpty()) {
        val node = stack.pop()

        if (node !in visited) {
            visited.add(node)
            graph[node]?.let { neighbors -> neighbors.filterNot { it in visited }.forEach{ stack.push(it) } }
        }
    }

    return visited
}

/**
 * The weights/distances are represented as doubles to avoid inaccuracy due to rounding
 * 
 * If we have a specific destination node in mind, pluck it out of the distances map 
 * that is returned. (distances.get(Pair<x,y>) =>)
 * 
 * Graph is a Pair (grid coordinates) => adjacency list of Pair and its weight/distance
 *  Example graph:
 *      Map(
 *       Pair(0, 0) to [ Pair( Pair(1, 0), 1.0), Pair( Pair(0, 1), 1.0)]
 *       Pair(1, 0) to [ Pair( Pair( 1, 4), 4.0)]
 *       etc
 *      )
 */
fun dijkstraWithLoopBackCapability(graph: Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Double>>>, start: Pair<Int, Int>): Map<Pair<Int, Int>, Double> {
    // stores the shortest path from the start node to every other node,
    val distances = mutableMapOf<Pair<Int, Int>, Double>().withDefault { Double.MAX_VALUE }
    // order nodes by their distance so that we process the nearest nodes first.
    val priorityQueue = PriorityQueue<Pair<Pair<Int, Int>, Double>>(compareBy { it.second })
    // keep track of the nodes we have visited
    val visited = mutableSetOf<Pair<Pair<Int, Int>, Double>>()

    priorityQueue.add(start to 0.0)
    distances[start] = 0.0

    while (priorityQueue.isNotEmpty()) {
        val (node, currentDist) = priorityQueue.poll()
        if (visited.add(node to currentDist)) {
            graph[node]?.forEach { (adjacent, weight) ->
                val totalDist = currentDist + weight
                if (totalDist < distances.getValue(adjacent)) {
                    distances[adjacent] = totalDist
                    priorityQueue.add(adjacent to totalDist)
                }
            }
        }
    }
    return distances
}

fun adjacent(p: Pair<Int, Int>, useDiags: Boolean = false, positiveOnly: Boolean = true): List<Pair<Int, Int>> {
    var neighbors = mutableListOf<Pair<Int, Int>>()
    // above
    neighbors.add(Pair(p.first, p.second + 1))
    // below
    neighbors.add(Pair(p.first, p.second - 1))
    // left
    neighbors.add(Pair(p.first - 1, p.second))
    // right
    neighbors.add(Pair(p.first + 1, p.second))

    if (useDiags) {
        // above, left
        neighbors.add(Pair(p.first - 1, p.second + 1))
        // above, right
        neighbors.add(Pair(p.first + 1, p.second + 1))
        // below, left
        neighbors.add(Pair(p.first - 1, p.second - 1))
        // below, right
        neighbors.add(Pair(p.first + 1, p.second - 1))
    }

    if (positiveOnly) {
        return neighbors.filter{ it.first > -1 && it.second > -1}
    }

    return neighbors
}

