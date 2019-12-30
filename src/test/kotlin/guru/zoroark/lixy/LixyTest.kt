package guru.zoroark.lixy

import kotlin.test.*

class LixyTest {
    @Test
    fun `Empty Lixy should crash`() {
        assertFailsWith<LixyException> {
            lixy {}
        }
    }

    @Test
    fun `Lixy constructs single unlabeled state`() {
        val ret = lixy {
            state {}
        }

        assertEquals(ret.states.size, 1)
    }

    @Test
    fun `Lixy is able to lex simple unlabeled state`() {
        val simpleStateDot = LixyTokenType()
        val lexer = lixy {
            state {
                "." isToken simpleStateDot
            }
        }
        val tokens = lexer.tokenize("....")
        assertEquals(
            tokens,
            (0 until 4).map { i ->
                LixyToken(
                    string = ".", startsAt = i, endsAt = i + 1,
                    tokenType = simpleStateDot
                )
            }
        )
    }
}