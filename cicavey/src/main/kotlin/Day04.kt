import java.io.File
import kotlin.math.min
import kotlin.math.max

fun check(grid: SparseGrid<Char>, p: Point2, term: String): Int {
    val raw = term.toCharArray()

    var count = 0
    // Try each direction
    for (d in Vec2.CARDINALS) {
        var cur = Vec2(p)
        var match = true
        for(i in raw.indices) {
            if(grid.get(cur.toPoint()) == raw[i]) {
                cur += d
            } else {
                match = false
                break
            }
        }
        if(match) {
            count++
        }
    }

    return count
}

fun xcheck(grid: SparseGrid<Char>, p: Point2): Int {
    // Must start on an A
    if(grid.get(p) != 'A') {
        return 0
    }

    val urv = grid.get(p + Vec2(1, -1))
    val ulv = grid.get(p + Vec2(-1, -1))
    val lrv = grid.get(p + Vec2(1, 1))
    val llv = grid.get(p + Vec2(-1, 1))

    val d1 = llv == 'S' && urv == 'M' || llv == 'M' && urv == 'S'
    val d2 = ulv == 'S' && lrv == 'M' || ulv == 'M' && lrv == 'S'

    return if (d1 && d2) 1 else 0
}

fun main() {

    val grid = SparseGrid<Char>('.')

    File("src/main/kotlin/Day04.txt").readLines().forEachIndexed { y, line ->
        line.toCharArray().forEachIndexed { x, c ->
            grid.set(x, y, c)
        }
    }

    val c = grid.p().sumOf {
        check(grid, it, "XMAS")
    }
    println(c)

    val c2 = grid.p().sumOf {
        xcheck(grid, it)
    }
    println(c2)
}