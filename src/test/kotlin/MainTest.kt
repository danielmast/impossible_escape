import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.util.*

internal class MainTest {

    @Test
    fun `2^5 = 32`() {
        assertEquals(32, pow2(5))
    }

    @Test
    fun `log2(32) = 5`() {
        assertEquals(5, log2(32))
    }

    @Test
    fun `Computing log2(28) throws an exception because 28 is not a power of two`() {
        assertThrows(IllegalArgumentException::class.java) { log2(28) }
    }

    @Test
    fun `32 is a power of two`() {
        assertTrue(32.isPowerOfTwo())
    }

    @Test
    fun `28 is not a power of two`() {
        assertFalse(28.isPowerOfTwo())
    }

    @Test
    fun `changing a copied BitSet does not change the original`() {
        val original = intArrayOf(3, 5, 8).toBitSet()

        val copy = original.copy().apply {
            set(3, false)
            set(7)
        }

        val expectedOriginal = intArrayOf(3, 5, 8).toBitSet()
        val expectedCopy = intArrayOf(5, 7, 8).toBitSet()

        assertEquals(expectedOriginal, original)
        assertEquals(expectedCopy, copy)
    }

    @Test
    fun `13 to BitSet equals 1101`() {
        val value = 13
        val expected = intArrayOf(0, 2, 3).toBitSet()
        assertEquals(expected, value.toBitSet())

    }

    @Test
    fun `63 to BitSet equals 111111`() {
        val value = 63
        val expected = IntArray(6) { it }.toBitSet()
        assertEquals(expected, value.toBitSet())
    }

    @Test
    fun `1101 to Int equals 13`() {
        val value = intArrayOf(0, 2, 3).toBitSet()
        val expected = 13
        assertEquals(expected, value.toInt())
    }

    @Test
    fun `111111 to Int equals 63`() {
        val expected = 63
        val value = IntArray(6) { it }.toBitSet()
        assertEquals(expected, value.toInt())
    }

    @Test
    fun `34 is in regions 1 and 5`() {
        val value = 34
        assertFalse(value.isInRegion(0))
        assertTrue(value.isInRegion(1))
        assertFalse(value.isInRegion(2))
        assertFalse(value.isInRegion(3))
        assertFalse(value.isInRegion(4))
        assertTrue(value.isInRegion(5))
    }

    @Test
    fun `63 is in all regions`() {
        val value = 63
        assertTrue(value.isInRegion(0))
        assertTrue(value.isInRegion(1))
        assertTrue(value.isInRegion(2))
        assertTrue(value.isInRegion(3))
        assertTrue(value.isInRegion(4))
        assertTrue(value.isInRegion(5))
    }

    @Test
    fun parity() {
        val value = intArrayOf(10, 13, 20, 30, 34, 45, 49, 58, 62).toBitSet()

        val expected = intArrayOf(0, 1, 2, 4, 5).toBitSet()

        assertEquals(expected, value.parity())
    }

    @Test
    fun `board with only the last field set to 1 has parity 111111`() {
        val value = intArrayOf(63).toBitSet()

        val expected = BitSet().apply {
            (0 until 6).forEach { set(it) }
        }

        assertEquals(expected, value.parity())
    }
}