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

    @Test
    fun `Ignore matcher on string in multiple states`() {
        val tkey = tokenType()
        val tvalue = tokenType()
        val svalue = stateLabel()
        val lexer = lixy {
            default state {
                matches("[a-zA-Z_ ]+") isToken tkey
                "->".ignore thenState svalue
            }
            svalue state {
                matches("[a-zA-Z_ ]+") isToken tvalue
                "\n".ignore thenState default
            }
        }
        val string = """
            key->value
            one->two
            Example_Yes->B e e_
        """.trimIndent()
        val expected = listOf(
            LixyToken("key", 0, 3, tkey),
            LixyToken("value", 5, 10, tvalue),
            LixyToken("one", 11, 14, tkey),
            LixyToken("two", 16, 19, tvalue),
            LixyToken("Example_Yes", 20, 31, tkey),
            LixyToken("B e e_", 33, 39, tvalue)
        )
        val result = lexer.tokenize(string)
        assertEquals(expected, result)
    }
}

