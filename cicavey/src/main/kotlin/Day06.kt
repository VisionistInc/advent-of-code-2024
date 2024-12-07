import java.io.File

data class Guard(var pos: Point2, var dir: Vec2)

fun main() {

    val grid = SparseGrid<Char>('.')
    var guardPos = Point2(0, 0)
    var guardDir = Vec2.N

    File("src/main/kotlin/Day06.txt").readLines().forEachIndexed { y, line ->
        line.toCharArray().forEachIndexed { x, c ->
            if(c == '^') {
                guardPos = Point2(x, y)
            } else {
                grid.set(x, y, c)
            }
        }
    }

    val g = Guard(guardPos, guardDir)
    val visited = mutableSetOf<Point2>()

    val exitPath = SparseGrid<Char>('.')
    exitPath.set(g.pos, '|')

    while(grid.inBounds(g.pos)) {
        val p = exitPath.get(g.pos)
        exitPath.set(g.pos, when(p) {
            '.' -> if (g.dir.isVert()) '|' else '-'
            '|', '-' -> '+'
            else -> p
        })
        visited.add(g.pos)
        var next = g.pos + g.dir
        while(grid.get(next) != '.') {
            g.dir = Vec2.nextDir(g.dir)
            exitPath.set(g.pos, '+')
            next = g.pos + g.dir
        }
        g.pos = g.pos + g.dir
    }

    println(visited.size) // 5404

    val potentialObstructions = exitPath.filteredPairs{ it != '^'}.map { pair ->  pair.first}.filter { it != guardPos }.toList()
//    val potentialObstructions = grid.p().filter { it != guardPos}.toList()
//    val potentialObstructions = grid.p().toList()

    // For each obstruction, add to copy of original, run sim looking for loop
    val obCount = potentialObstructions.sumOf {
        val localGrid = grid.copy()
        localGrid.set(it, 'O')
        val g = Guard(guardPos, guardDir)
        val visitedDir = mutableSetOf<Pair<Point2, Vec2>>()
        var loop = false

        while(localGrid.inBounds(g.pos)) {
            val v = Pair(g.pos, g.dir)
            if(visitedDir.contains(v)) {
                loop = true
                break
            }
            visitedDir.add(v)
            var next = g.pos + g.dir
            while(localGrid.get(next) != '.') {
                g.dir = Vec2.nextDir(g.dir)
                visitedDir.add(Pair(g.pos, g.dir))
                next = g.pos + g.dir
            }
            g.pos = g.pos + g.dir
        }
        if(loop) 1L else 0L
    }

    println(obCount)
}