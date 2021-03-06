import java.util.*
import kotlin.math.pow
import kotlin.system.measureTimeMillis

const val DIM_EXP: Int = 4 // Only edit this value

val dim = 2.0.pow(DIM_EXP).toInt()
val n = pow(2, dim)

val vertices = Array(n) { i -> Vertex(i) }
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

    while (history.size < n) {
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
        if (vertex.neighbours().map { it.color }.distinct().size != dim) {
            throw IllegalStateException("$vertex has illegal neighbours")
        }
    }
}

fun printResults(elapsed: Long) {
    println("Vertices:")
    vertices.forEach{ println(it) }
    println("Dim: $dim, N = $n ($elapsed ms)")
}

data class Vertex(
        val key: Int,
        var color: Int? = null
) {
    fun neighbours() =
        (0 until dim).map { key xor pow(2, it) }.map{ vertices[it] }

    fun allowedColors() =
            (0 until dim).minus(colorsOfSecondDegreeNeighbours())

    private fun colorsOfSecondDegreeNeighbours() =
            neighbours().flatMap { it.neighbours() }.distinct().filter { it != this }.map { it.color }.sortedBy { it }
}

fun pow(base: Int, exp: Int) = base.toDouble().pow(exp).toInt()