package guru.zoroark.lixy

import kotlin.test.*

class RangeRecognizerTests {
    @Test
    fun `Range can be used as bare recognizer`() {
        val ta = tokenType()
        val tdigit = tokenType()
        val lexer = lixy {
            state {
                "a" isToken ta
                '0'..'9' isToken tdigit
            }
        }
        val string = "a1aa4a12345a67a890"
        val expected = listOf(
            LixyToken("a", 0, 1, ta),
            LixyToken("1", 1, 2, tdigit),
            LixyToken("a", 2, 3, ta),
            LixyToken("a", 3, 4, ta),
            LixyToken("4", 4, 5, tdigit),
            LixyToken("a", 5, 6, ta),
            LixyToken("1", 6, 7, tdigit),
            LixyToken("2", 7, 8, tdigit),
            LixyToken("3", 8, 9, tdigit),
            LixyToken("4", 9, 10, tdigit),
            LixyToken("5", 10, 11, tdigit),
            LixyToken("a", 11, 12, ta),
            LixyToken("6", 12, 13, tdigit),
            LixyToken("7", 13, 14, tdigit),
            LixyToken("a", 14, 15, ta),
            LixyToken("8", 15, 16, tdigit),
            LixyToken("9", 16, 17, tdigit),
            LixyToken("0", 17, 18, tdigit)
        )
        val actual = lexer.tokenize(string)
        assertEquals(expected, actual)
    }
}