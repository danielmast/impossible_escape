import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.pow
import java.io.File
import kotlin.math.sqrt

val n = getBoardFromFile().size()

fun main() {
    when (getModeFromFile()) {
        1 -> actionPrisoner1()
        2 -> actionPrisoner2()
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

fun getModeFromFile(): Int =
    object {}.javaClass.getResource("mode.txt").readText().replace("\n", "").toInt()

fun getKeyFromFile(): Int =
    object {}.javaClass.getResource("key.txt").readText().replace("\n", "").toInt()

fun Int.isPowerOfTwo() = (this and (this - 1)) == 0

fun Int.isInRegion(region: Int) = toBitSet().get(region)

fun Int.toBitSet(): BitSet =
    BitSet().let {
        var index = 0
        var value = this
        while (value != 0) {
            if (value % 2 != 0) {
                it.set(index)
            }
            index++
            value = value shr 1
        }
        it
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
