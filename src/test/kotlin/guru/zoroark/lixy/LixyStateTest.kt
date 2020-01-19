package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.LixyStringTokenMatcher
import kotlin.test.*

class LixyStateTest {
    @Test
    fun `Lixy supports constructing one default state`() {
        val dottoken = tokenType()
        val lixy = lixy {
            default state {
                "." isToken dottoken
            }
        }

        val tokens = lixy.tokenize("...")
        assertEquals(
            listOf(
                LixyToken(".", 0, 1, dottoken),
                LixyToken(".", 1, 2, dottoken),
                LixyToken(".", 2, 3, dottoken)
            ),
            tokens
        )
    }

    @Test
    fun `Lixy fails to construct unlabeled then labeled default`() {
        val ttype = tokenType()
        assertFailsWith<LixyException> {
            lixy {
                state {
                    "." isToken ttype
                }
                default state {
                    "." isToken ttype
                }
            }
        }
    }

    @Test
    fun `Lixy fails to construct labeled default then unlabeled`() {
        val ttype = tokenType()
        assertFailsWith<LixyException> {
            lixy {
                default state {
                    "." isToken ttype
                }
                state {
                    "." isToken ttype
                }
            }
        }
    }

    @Test
    fun `Lixy fails to construct multiple unlabeled states`() {
        val ttype = tokenType()
        assertFailsWith<LixyException> {
            lixy {
                state {
                    "." isToken ttype
                }
                state {
                    "." isToken ttype
                }
            }
        }
    }

    @Test
    fun `Lixy fails to construct multiple default states`() {
        val ttype = tokenType()
        assertFailsWith<LixyException> {
            lixy {
                default state {
                    "." isToken ttype
                }
                default state {
                    "." isToken ttype
                }
            }
        }
    }

    @Test
    fun `Lixy successfully constructs multiple states and starts on default`() {
        val one = tokenType()
        val two = tokenType()
        val other = stateLabel()

        val lexer = lixy {
            default state {
                "1" isToken one
            }
            other state {
                "2" isToken two
            }
        }
        assertEquals(2, lexer.statesCount)

        // Check contents of default state
        val defState = lexer.defaultState
        assertEquals(1, defState.matchers.size)
        val oneMatcher = defState.matchers[0] as? LixyStringTokenMatcher
            ?: error("Incorrect matcher type")
        assertEquals("1", oneMatcher.match)

        // Check contents of other state
        val oState = lexer.getState(other)
        assertEquals(1, oState.matchers.size)
        val twoMatcher = oState.matchers[0] as? LixyStringTokenMatcher
            ?: error("Incorrect matcher type")
        assertEquals("2", twoMatcher.match)

        // Check that first state is used
        val result = lexer.tokenize("1")
        assertEquals(listOf(LixyToken("1", 0, 1, one)), result)

        // Also check that trying to match thing from second state fails
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize("12")
        }
    }
}