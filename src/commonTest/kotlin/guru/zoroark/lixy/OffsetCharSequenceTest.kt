package guru.zoroark.lixy

import kotlin.test.*


class OffsetCharSequenceTest {
    @Test
    fun offset_char_sequence_length() {
        assertEquals("hello".offsetBy(3).length, 2)
    }

    @Test
    fun offset_char_sequence_throws_error_if_incorrect_offset() {
        assertFails {
            "hey".offsetBy(4)
        }
    }

    @Test
    fun offset_char_sequence_correct() {
        val offset = "hello".offsetBy(2)
        assertEquals(offset[0], 'l')
        assertEquals(offset[1], 'l')
        assertEquals(offset[2], 'o')
        assertFailsWith<IndexOutOfBoundsException> { offset[3] }
    }
}