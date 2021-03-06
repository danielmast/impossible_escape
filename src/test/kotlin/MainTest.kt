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
    fun `13 to BitSet equals 1101`() {
        val value = 13
        val expected = BitSet().apply {
            set(0)
            set(2)
            set(3)
        }
        assertEquals(expected, value.toBitSet())

    }

    @Test
    fun `63 to BitSet equals 111111`() {
        val value = 63
        val expected = BitSet(64)
        (0 until 6).forEach { expected.set(it) }
        assertEquals(expected, value.toBitSet())
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
        val value = BitSet().apply {
            set(10)
            set(13)
            set(20)
            set(30)
            set(34)
            set(45)
            set(49)
            set(58)
            set(62)
        }

        val expected = BitSet().apply {
            set(0)
            set(1)
            set(2)
            set(4)
            set(5)
        }

        assertEquals(expected, value.parity())
    }

    @Test
    fun `board with only the last field set to 1 has parity 111111`() {
        val value = BitSet().apply {
            set(63)
        }

        val expected = BitSet().apply {
            (0 until 6).forEach { set(it) }
        }

        assertEquals(expected, value.parity())
    }
}