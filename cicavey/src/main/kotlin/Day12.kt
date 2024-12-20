import java.io.File
import kotlin.collections.getOrPut

fun contig(g: SparseGrid<Char>, start: Point2): MutableSet<Point2> {
    val r = mutableSetOf<Point2>()
    contigDFS(g, r, start, g.get(start))
    return r
}

fun contigDFS(g: SparseGrid<Char>, s: MutableSet<Point2>, p: Point2, v: Char) {
    if(!g.inBounds(p) || p in s || g.get(p) != v) {
        return
    }
    s.add(p)
    contigDFS(g, s, p + Vec2.N, v)
    contigDFS(g, s, p + Vec2.E, v)
    contigDFS(g, s, p + Vec2.S, v)
    contigDFS(g, s, p + Vec2.W, v)
}

fun perimeter(g: SparseGrid<Char>, region: Set<Point2>): Long {
    val v = g.get(region.iterator().next())
    return region.sumOf { p ->
        Vec2.DIR.sumOf { d ->
            if(!g.inBounds(p + d) || g.get(p + d) != v) {
                1L
            } else {
                0L
            }
        }
    }
}

fun main() {
    val grid = SparseGrid('.')
    File("src/main/kotlin/Day12.txt").readLines().forEachIndexed { y, line ->
        line.toCharArray().forEachIndexed { x, c ->
            grid.set(x, y, c)
        }
    }

    val regions = mutableListOf<Pair<Char, MutableSet<Point2>>>()
    val consumed = mutableSetOf<Point2>()

    for(p in grid.p()) {
        if(p in consumed) {
            continue
        }

        val region = contig(grid, p)

        regions.add(grid.get(p) to region)

        consumed.addAll(region)
    }

    println(regions.sumOf { it.second.size  * perimeter(grid, it.second)})

    val p2 = regions.sumOf { (v, pts) ->
        // pretend the region is in a grid alone
        val lg = SparseGrid('.')
        lg.setAll(pts, v)
        var c = 0L
        for(p in pts) {
            val nval = lg.get(p + Vec2.N)
            val sval = lg.get(p + Vec2.S)
            val eval = lg.get(p + Vec2.E)
            val wval = lg.get(p + Vec2.W)

            val neval = lg.get(p + Vec2.NE)
            val seval = lg.get(p + Vec2.SE)
            val nwval = lg.get(p + Vec2.NW)
            val swval = lg.get(p + Vec2.SW)

            // Outside corners
            if(nval == eval && nval != v) {
                c++
            }
            if(nval == wval && nval != v) {
                c++
            }
            if(sval == eval && sval != v) {
                c++
            }
            if(sval == wval && sval != v) {
                c++
            }

            // Insider corners
            if(nval == eval && nval == v && neval != v) {
                c++
            }
            if(nval == wval && nval == v && nwval != v) {
                c++
            }
            if(sval == eval && sval == v && seval != v) {
                c++
            }
            if(sval == wval && sval == v && swval != v) {
                c++
            }

        }
        c * pts.size
    }
    println(p2)
}