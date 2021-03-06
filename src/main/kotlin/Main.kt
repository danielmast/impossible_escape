import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.pow
import kotlin.system.measureTimeMillis

const val EXP: Int = 4 // Only edit this value

val n = pow2(EXP)
val v = pow2(n)

val vertices = Array(v) { i -> Vertex(i) }
val history: Stack<Vertex> = Stack()

fun main() {
    val elapsed = measureTimeMillis {
        colorGraph()
        validateGraph()
    }
    printResults(elapsed)
}

fun colorGraph() {
    history.push(vertices[0].apply { color = 0 })

    while (history.size < v) {
        val colorlessNeighbour = history.peek().neighbours().first { it.color == null }

        if (colorlessNeighbour.allowedColors().isNotEmpty()) {
            history.push(colorlessNeighbour.apply {
                color = allowedColors().first()
            })
        } else {
            while (history.peek().let { it.color == it.allowedColors().last() }) {
                history.pop().apply { color = null }
            }
            history.peek().apply {
                color = allowedColors()[allowedColors().indexOf(color) + 1]
            }
        }
    }
}

fun validateGraph() {
    vertices.forEach { vertex ->
        if (vertex.color == null) {
            throw IllegalStateException("$vertex has no color")
        }
        if (vertex.neighbours().map { it.color }.distinct().size != n) {
            throw IllegalStateException("$vertex has illegal neighbours")
        }
    }
}

fun printResults(elapsed: Long) {
    println("Vertices:")
    vertices.forEach{ println(it) }
    println("N: $n, V = $v ($elapsed ms)")
}

data class Vertex(
        val index: Int,
        var color: Int? = null
) {
    fun neighbours() =
        (0 until n).map { index xor pow2(it) }.map{ vertices[it] }

    fun allowedColors() =
            (0 until n).minus(colorsOfSecondDegreeNeighbours())

    private fun colorsOfSecondDegreeNeighbours() =
            neighbours().flatMap { it.neighbours() }.distinct().filter { it != this }.map { it.color }.sortedBy { it }
}

fun pow2(exp: Int) = 2.0.pow(exp).toInt()

fun log2(x: Int) = x.let {
    if (!it.isPowerOfTwo()) throw IllegalArgumentException("$it should be power of two")
    kotlin.math.log2(it.toDouble()).toInt()
}

fun Int.isPowerOfTwo() = (this and (this - 1)) == 0

fun BitSet.parity(): BitSet {
    val result = BitSet(log2(this.size()))

    (0 until this.size()).map { index ->
        val bit = this.get(index)
        if (bit) {
            (0 until log2(this.size())).map { region ->
                if (index.isInRegion(region)) result.flip(region)
            }
        }
    }
    return result
}

fun Int.isInRegion(region: Int) = this.toBitSet().get(region)

fun Int.toBitSet(): BitSet {
    val result = BitSet()
    var index = 0
    var value = this
    while (value != 0) {
        if (value % 2 != 0) {
            result.set(index)
        }
        index++
        value = value shr 1
    }
    return result
}