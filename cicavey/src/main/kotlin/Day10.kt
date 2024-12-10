import java.io.File

fun follow(g: SparseGrid<Int>, cur: Int, p: Point2, summits: MutableSet<Point2>): Int {
    if(cur == 9) {
        summits.add(p)
        return 1
    }
    var sum = 0
    var np = p + Vec2.N
    if(g.inBounds(np) && g.get(np) == cur + 1) {
        sum += follow(g, g.get(np), np, summits)
    }
    np = p + Vec2.S
    if(g.inBounds(np) && g.get(np) == cur + 1) {
        sum += follow(g, g.get(np), np, summits)
    }
    np = p + Vec2.E
    if(g.inBounds(np) && g.get(np) == cur + 1) {
        sum += follow(g, g.get(np), np, summits)
    }
    np = p + Vec2.W
    if(g.inBounds(np) && g.get(np) == cur + 1) {
        sum += follow(g, g.get(np), np, summits)
    }
    return sum
}

fun followHelper(g: SparseGrid<Int>, p: Point2): Pair<Int, Int> {
    val cur = g.get(p)
    val summits = mutableSetOf<Point2>()
    val trails = follow(g, cur, p, summits)
    return summits.size to trails
}

fun main() {
    val grid = SparseGrid<Int>(-1)
    File("src/main/kotlin/Day10.txt").readLines().forEachIndexed { y, line ->
        line.toCharArray().forEachIndexed { x, c ->
            if (c != '.') {
                grid.set(x, y, c - '0')
            }
        }
    }

    var p1 = 0
    var p2 = 0

    grid.filteredPairs { it == 0 }.forEach { pair ->
        val res = followHelper(grid, pair.first)
        p1 += res.first
        p2 += res.second
    }
    println(p1)
    println(p2)
}