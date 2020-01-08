package guru.zoroark.lixy

import kotlin.test.*


class OffsetCharSequenceTest {
    @Test
    fun `Offset char sequence length`() {
        assertEquals("hello".offsetBy(3).length, 2)
    }

    @Test
    fun `Offset char sequence throws error if incorrect offset`() {
        assertFails {
            "hey".offsetBy(4)
        }
    }

    @Test
    fun `Offset char sequence correct`() {
        val offset = "hello".offsetBy(2)
        assertEquals(offset[0], 'l')
        assertEquals(offset[1], 'l')
        assertEquals(offset[2], 'o')
        assertFailsWith<StringIndexOutOfBoundsException> { offset[3] }
    }
}