package guru.zoroark.lixy

import kotlin.test.*

class IgnoreMatcherTest {
    @Test
    fun `Ignore matcher on string in single state`() {
        val tone = tokenType()
        val ttwo = tokenType()
        val lexer = lixy {
            state {
                "a" isToken tone
                "b" isToken ttwo
                " ".ignore
            }
        }
        val string = "ab ba aa bb"
        val expected = listOf(
            LixyToken("a", 0, 1, tone),
            LixyToken("b", 1, 2, ttwo),
            LixyToken("b", 3, 4, ttwo),
            LixyToken("a", 4, 5, tone),
            LixyToken("a", 6, 7, tone),
            LixyToken("a", 7, 8, tone),
            LixyToken("b", 9, 10, ttwo),
            LixyToken("b", 10, 11, ttwo)
        )
        val result = lexer.tokenize(string)
        assertEquals(expected, result)
    }
}

