import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.pow
import Mode.*
import java.io.File
import kotlin.math.sqrt

const val EXP: Int = 6 // Only edit this value
val n = pow2(EXP)

val mode = PRISONER_1 // Choose a mode
enum class Mode { PRISONER_1, PRISONER_2 }

fun main() {
    when (mode) {
        PRISONER_1 -> actionPrisoner1()
        PRISONER_2 -> actionPrisoner2()
    }
}

fun actionPrisoner1() {
    getKeyFromFile().let { key ->
        getBoardFromFile().let { board ->
            board.parity().apply { xor(key.toBitSet()) }.toInt().let { flip ->
                board.flip(flip)
                saveBoardToFile(board)
                println("Prisoner 1 flipped coin $flip")
            }
        }
    }
}

fun actionPrisoner2() {
    getBoardFromFile().parity().toInt().let { key ->
        println("Prisoner 2 guesses that key is at position $key")
    }
}

fun getBoardFromFile(): BitSet =
    object {}.javaClass.getResource("board.txt").readText().replace("\n", "").let { content ->
        if (content.length != n) throw IllegalArgumentException("Board should consist of $n bits")
        BitSet(n).apply {
            content.forEachIndexed { index, bit ->
                if (bit == '1') set(index)
            }
        }
    }

fun saveBoardToFile(board: BitSet) {
    File("src/main/resources/board.txt").writeText(
        (0 until n).mapIndexed { index, bit ->
            (if (board.get(bit)) "1" else "0") + (if ((index + 1) % sqrt(n.toDouble()).toInt() == 0) "\n" else "")
        }.joinToString("")
    )
}

fun getKeyFromFile(): Int =
    object {}.javaClass.getResource("key.txt").readText().replace("\n", "").toInt()

fun Int.isPowerOfTwo() = (this and (this - 1)) == 0

fun Int.isInRegion(region: Int) = toBitSet().get(region)

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

fun IntArray.toBitSet() =
    BitSet(size).apply {
        this@toBitSet.forEach { set(it) }
    }

fun BitSet.copy() = BitSet(size()).apply {
    (0 until this@copy.size()).forEach {
        set(it, this@copy.get(it))
    }
}

fun BitSet.toInt(): Int =
    (0 until size()).fold(0) { acc, index ->
        acc + if (get(index)) pow2(index) else 0 }

fun BitSet.parity(): BitSet {
    val result = BitSet(log2(size()))

    (0 until size()).map { index ->
        val bit = get(index)
        if (bit) {
            (0 until log2(size())).map { region ->
                if (index.isInRegion(region)) result.flip(region)
            }
        }
    }
    return result
}

fun pow2(exp: Int) = 2.0.pow(exp).toInt()

fun log2(x: Int) = x.let {
    if (!it.isPowerOfTwo()) throw IllegalArgumentException("$it should be power of two")
    kotlin.math.log2(it.toDouble()).toInt()
}
