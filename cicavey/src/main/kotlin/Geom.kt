import kotlin.math.max
import kotlin.math.min

data class Point2(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
    operator fun plus(v: Vec2) = Point2(x + v.x, y + v.y)
    operator fun minus(v: Vec2) = Point2(x - v.x, y - v.y)
    operator fun minus(p: Point2) = Vec2(x - p.x, y - p.y)
}

data class Vec2(val x: Long, val y: Long) {
    constructor(p: Point2) : this(p.x, p.y) {}
    operator fun plus(v: Vec2) = Vec2(x + v.x, y + v.y)
    operator fun minus(v: Vec2) = Point2(x - v.x, y - v.y)
    operator fun times(s: Long) = Vec2(x * s, y * s)
    fun toPoint() = Point2(x, y)

    fun isVert() = y != 0L && x == 0L
    fun isHorz() = y == 0L && x != 0L
    fun isN() = y < 0
    fun isS() = y > 0
    fun isE() = x > 0
    fun isW() = x < 0

    companion object {
        val N = Vec2(0, -1)
        val S = Vec2(0, 1)
        val E = Vec2(1, 0)
        val W = Vec2(-1, 0)
        val NE = Vec2(1, -1)
        val SE = Vec2(1, 1)
        val SW = Vec2(-1, 1)
        val NW = Vec2(-1, -1)

        val DIR = listOf(N, E, S, W)
        val CARDINALS = listOf(N, NE, E, SE, S, SW, W, NW)

        /**
         * Get the next direction, assuming a 90 deg right turn
         */
        fun nextDir(dir: Vec2) = DIR[(DIR.indexOf(dir) + 1) % DIR.size]
    }
}

class SparseGrid<T>(val defaultValue: T) {
    val data = HashMap<Point2, T>()
    var minX = 0L
    var maxX = 0L
    var minY = 0L
    var maxY = 0L

    fun bounds(minX: Long, maxX: Long, minY: Long, maxY: Long) {
        this.minX = minX
        this.maxX = maxX
        this.minY = minY
        this.maxY = maxY
    }

    fun get(x: Long, y: Long) = data.getOrDefault(Point2(x, y), defaultValue)

    fun get(p: Point2) = get(p.x, p.y)

    fun set(x: Long, y: Long, value: T) {
        data[Point2(x, y)] = value
        minX = min(minX, x)
        maxX = max(maxX, x)
        minY = min(minY, y)
        maxY = max(maxY, y)
    }

    fun enlarge(inc: Long = 1L) {
        minX -= inc
        maxX += inc
        minY -= inc
        maxY += inc
    }

    fun set(x: Int, y: Int, value: T) {
        set(x.toLong(), y.toLong(), value)
    }

    fun set(p: Point2, value: T) {
        set(p.x, p.y, value)
    }

    fun setAll(p: Iterable<Point2>, value: T) {
        p.forEach { set(it, value)}
    }

    fun y() = LongRange(minY, maxY)

    fun x() = LongRange(minX, maxX)

    fun p(): Iterable<Point2> = sequence {
        for (row in y()) {
            for (col in x()) {
                yield(Point2(row, col))
            }
        }
    }.asIterable()

    fun pairs(): Iterable<Pair<Point2, T>> = data.entries.map { Pair(it.key, it.value) }
    fun filteredPairs(pred: ((T) -> Boolean)): Iterable<Pair<Point2, T>> = data.entries.filter { pred(it.value)}.map { Pair(it.key, it.value) }

    fun inBounds(vararg pts: Point2): Boolean {
        return pts.all { p ->
            p.x >= minX && p.y >= minY && p.x <= maxX && p.y <= maxY
        }
    }

    fun toBoundedString(mapper: ((T) -> String)? = null): String {
        val sb = StringBuilder()
        for(y in minY..maxY) {
            for(x in minX..maxX) {
                val v = get(x, y)
                sb.append(mapper?.invoke(v) ?: v)
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun copy(): SparseGrid<T> {
        val grid = SparseGrid<T>(defaultValue)
        grid.data.putAll(data)
        grid.minX = minX
        grid.minY = minY
        grid.maxX = maxX
        grid.maxY = maxY
        return grid
    }

    operator fun plus(g: SparseGrid<T>): SparseGrid<T> {
        val c = copy()
        c.data.putAll(g.data)
        c.minX = min(c.minX, g.minX)
        c.maxX = max(c.maxX, g.maxX)
        c.minY = min(c.minY, g.minY)
        c.maxY = max(c.maxY, g.maxY)
        return c
    }
}