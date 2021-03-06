import java.lang.IllegalArgumentException
import java.util.*
import kotlin.math.pow

const val EXP: Int = 6 // Only edit this value

val n = pow2(EXP)

fun main() {
    val board = getBoardFromFile()
    val key = Random().nextInt(n)
    println("Warden hides key at position $key")
    actionPrisoner1(board, key)
    val foundKey = actionPrisoner2(board)
    println("Prisoner 2 guesses that key is at position $foundKey")
}

fun getBoardFromFile(): BitSet =
    object {}.javaClass.getResource("board.csv").readText().replace("\n", "").let { content ->
        if (content.length != n) throw IllegalArgumentException("Board should consist of $n bits")
        BitSet(n).apply {
            content.forEachIndexed { index, bit ->
                if (bit == '1') set(index)
            }
        }
    }

fun getRandomBoard() = IntArray(n) { it }.filter { Random().nextBoolean() }.toIntArray().toBitSet()

fun actionPrisoner1(board: BitSet, key: Int) {
    val parity = board.parity()
    val diff = parity.copy().apply { xor(key.toBitSet()) }
    val flipped = diff.toInt()
    board.flip(flipped)
    println("Prisoner 1 flipped coin $flipped")
}

fun actionPrisoner2(board: BitSet) = board.parity().toInt()

fun pow2(exp: Int) = 2.0.pow(exp).toInt()

fun log2(x: Int) = x.let {
    if (!it.isPowerOfTwo()) throw IllegalArgumentException("$it should be power of two")
    kotlin.math.log2(it.toDouble()).toInt()
}

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