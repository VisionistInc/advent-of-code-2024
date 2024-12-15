import java.io.File

fun parse1(f : String): Triple<SparseGrid<Char>, Point2, MutableList<Vec2>> {
    var mode = 0
    val grid = SparseGrid('.')
    val ins = mutableListOf<Vec2>()
    var robot = Point2()
    File(f).readLines().forEachIndexed { y, line ->
        if(line.isBlank()) {
            mode = 1
            return@forEachIndexed
        }
        if(mode == 0) {
            line.toCharArray().forEachIndexed { x, c ->
                // Don't put the robot in the grid
                if(c == '@') {
                    robot = Point2(x, y)
                } else {
                    grid.set(x, y, c)
                }
            }
        } else {
            ins.addAll(line.trim().map {
                when(it) {
                    '>' -> Vec2.E
                    '<' -> Vec2.W
                    '^' -> Vec2.N
                    'v' -> Vec2.S
                    else -> throw Exception("Unexpected character: $it")
                }
            }.asIterable())
        }
    }

    return Triple(grid, robot, ins)
}

fun printGrid(grid : SparseGrid<Char>, robot: Point2? = null) {
    println(grid.overlay { p, v ->
        if(p == robot) '@' else v
    })
}

fun part1(s: String) {
    var (grid, robot, ins) = parse1(s)

    for(op in ins) {
        val next = grid.get(robot + op)

        // wall, robot can't move
        if(next == '#') {
            continue
        }

        // Robot moves into empty space
        if(next == '.') {
            robot += op
            continue
        }

        // Next must be a box
        var proj = robot.copy() + op + op
        var found = false
        while(true) {
            val l = grid.get(proj)
            if(l == '#') {
                break
            }
            if(l == '.') {
                found = true
                break
            }
            proj += op
        }

        // Can't push this series of boxes, no space, abort
        if(!found) {
            continue
        }

        // Shift all the boxes in this line over
        var bp = robot + op + op
        do {
            grid.set(bp, 'O')
            bp += op
        } while(bp != proj + op)

        grid.set(robot, '.')
        robot += op
        grid.set(robot, '.')
    }

    val p1 = grid.pairs().filter { it.second == 'O' }.sumOf { it.first.y * 100 + it.first.x }
    println(p1)
}

fun parse2(f : String): Triple<SparseGrid<Char>, Point2, MutableList<Vec2>> {
    var mode = 0
    val grid = SparseGrid('.')
    val ins = mutableListOf<Vec2>()
    var robot = Point2()
    File(f).readLines().forEachIndexed { y, line ->
        if(line.isBlank()) {
            mode = 1
            return@forEachIndexed
        }
        if(mode == 0) {
            line.toCharArray().forEachIndexed { x, c ->
                // Don't put the robot in the grid
                if(c == '@') {
                    robot = Point2(2 * x, y)
                } else {
                    when(c) {
                        'O' -> {
                            grid.set(2*x, y, '[')
                            grid.set(2*x + 1, y, ']')
                        }
                        else -> {
                            grid.set(2*x, y, c)
                            grid.set(2*x + 1, y, c)
                        }
                    }
                }
            }
        } else {
            ins.addAll(line.trim().map {
                when(it) {
                    '>' -> Vec2.E
                    '<' -> Vec2.W
                    '^' -> Vec2.N
                    'v' -> Vec2.S
                    else -> throw Exception("Unexpected character: $it")
                }
            }.asIterable())
        }
    }

    return Triple(grid, robot, ins)
}

fun part2(s: String) {
    var (grid, robot, ins) = parse2(s)
//    printGrid(grid, robot)
    ins.forEachIndexed { index, op ->

//        if(index != 0 ) { printGrid(grid, robot) }
//        println("$index: $op\n")

        val next = grid.get(robot + op)

        // wall, robot can't move
        if(next == '#') {
            return@forEachIndexed
        }

        // Robot moves into empty space
        if(next == '.') {
            robot += op
            return@forEachIndexed
        }

        // Recursively push the box, and conditionally move robot
        if(op.isHorz()) {
            if(pushH(grid, op, robot + op)) {
                robot += op
            }
        } else {

            val pts = if(next == '[') {
                listOf(robot + op, robot + op + Vec2.E)
            } else {
                listOf(robot + op, robot + op + Vec2.W)
            }

            if(pushV(grid, op, pts)) {
                robot += op
            }
        }
    }

    val p2 = grid.pairs().filter { it.second == '[' }.sumOf { it.first.y * 100 + it.first.x }
    println(p2)
}

fun pushH(g: SparseGrid<Char>, v: Vec2, p: Point2): Boolean {
    val next = g.get(p)
    if(next == '#') {
        return false
    }
    if(next == '.') {
        return true
    }
    // box width ...
    if(pushH(g, v, p + v + v)) {
        // shift box over
        g.set(p + v + v, g.get(p + v))
        g.set(p + v, g.get(p))
        g.set(p, '.')
        return true
    }

    return false
}

fun pushV(g: SparseGrid<Char>, v: Vec2, p: List<Point2>): Boolean {
    if(p.any { g.get(it) == '#' }) {
        return false
    }
    if(p.all { g.get(it) == '.' }) {
        return true
    }

    val newP = p.map { it + v }.flatMap {
        when(g.get(it)) {
            '[' -> listOf(it, it + Vec2.E)
            ']' -> listOf(it + Vec2.W, it)
            '#' -> listOf(it)
            else -> emptyList()// listOf(it)
        }
    }.distinct()

    if(newP.any { g.get(it) == '#' }) {
        return false
    }

    if(pushV(g, v, newP)) {
        val moveP = p.filter {
            g.get(it) == '[' || g.get(it) == ']'
        }
        moveP.forEach {
            g.set(it + v, g.get(it))
        }
        moveP.forEach { g.set(it, '.') }
        return true
    }

    return false
}

fun main() {
    val s = "src/main/kotlin/Day15.txt"
    part1(s)
    part2(s)
}