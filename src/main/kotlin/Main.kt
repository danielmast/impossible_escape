import java.util.*
import kotlin.math.pow
import kotlin.system.measureTimeMillis

const val EXP: Int = 4 // Only edit this value

val n = pow(2, EXP)
val v = pow(2, n)

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
        (0 until n).map { index xor pow(2, it) }.map{ vertices[it] }

    fun allowedColors() =
            (0 until n).minus(colorsOfSecondDegreeNeighbours())

    private fun colorsOfSecondDegreeNeighbours() =
            neighbours().flatMap { it.neighbours() }.distinct().filter { it != this }.map { it.color }.sortedBy { it }
}

fun pow(base: Int, exp: Int) = base.toDouble().pow(exp).toInt()