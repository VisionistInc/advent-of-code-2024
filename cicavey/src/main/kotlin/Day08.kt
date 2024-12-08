import java.io.File

private fun <A, B> cart(
    listA: Iterable<A>,
    listB: Iterable<B>
): Sequence<Pair<A, B>> =
    sequence {
        listA.forEach { a ->
            listB.forEach { b ->
                if (a != b) {
                    yield(a to b)
                }
            }
        }
    }

fun main() {
    val grid = SparseGrid<Char>('.')

    var h = 0L
    var w = 0L

    File("src/main/kotlin/Day08.txt").readLines().forEachIndexed { y, line ->
        h = y.toLong()
        w = line.length.toLong() - 1
        line.toCharArray().forEachIndexed { x, c ->
            if(c != '.') {
                grid.set(x, y, c)
            }
        }
    }

    grid.bounds(0, w, 0, h)

    val antinodes = hashSetOf<Point2>()

    cart(grid.pairs(), grid.pairs()).forEach { p ->
        // This will product dupes...
        if(p.second.second == p.first.second) {
            val pt1 = p.first.first
            val pt2 = p.second.first
            val offset = (pt1 - pt2) * 2L

            val an1 = pt1 - offset
            if(grid.inBounds(an1)) {
                antinodes.add(an1)
            }
            val an2 = pt2 + offset
            if(grid.inBounds(an2)) {
                antinodes.add(an2)
            }


        }
    }

    println(antinodes.size)

    val resantinodes = hashSetOf<Point2>()

    cart(grid.pairs(), grid.pairs()).forEach { p ->
        // This will product dupes...
        if(p.second.second == p.first.second) {
            val pt1 = p.first.first
            val pt2 = p.second.first
            val offset = (pt1 - pt2)// * 2L TRICKY READING

            var an1 = pt1 - offset
            while(grid.inBounds(an1)) {
                resantinodes.add(an1)
                an1 -= offset
            }
            var an2 = pt2 + offset
            while(grid.inBounds(an2)) {
                resantinodes.add(an2)
                an2 += offset
            }


        }
    }

    println(resantinodes.size)
}