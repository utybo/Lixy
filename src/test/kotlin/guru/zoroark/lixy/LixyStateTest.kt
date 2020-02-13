package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.LixyTokenRecognizerMatched
import guru.zoroark.lixy.matchers.LixyStringTokenRecognizer
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
    fun `Lixy cannot create two states with the same label`() {
        val a = stateLabel()
        val ta = tokenType()
        val tb = tokenType()
        val exc = assertFailsWith<LixyException> {
            lixy {
                default state {
                    "ab" isToken tb
                }
                a state {
                    "b" isToken tb
                }
                a state {
                    "a" isToken ta
                }
            }
        }
        assert(exc.message!!.contains("two states with the same label"))
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
        val oneMatcher =
            (defState.matchers[0] as? LixyTokenRecognizerMatched)?.recognizer as? LixyStringTokenRecognizer
                ?: error("Incorrect matcher type")
        assertEquals("1", oneMatcher.toRecognize)

        // Check contents of other state
        val oState = lexer.getState(other)
        assertEquals(1, oState.matchers.size)
        val twoMatcher =
            (oState.matchers[0] as? LixyTokenRecognizerMatched)?.recognizer as? LixyStringTokenRecognizer
                ?: error("Incorrect matcher type")
        assertEquals("2", twoMatcher.toRecognize)

        // Check that first state is used
        val result = lexer.tokenize("1")
        assertEquals(listOf(LixyToken("1", 0, 1, one)), result)

        // Also check that trying to match thing from second state fails
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize("12")
        }
    }

    @Test
    fun `Lixy supports switching from state to state`() {
        val one = tokenType()
        val two = tokenType()
        val other = stateLabel()

        val lexer = lixy {
            default state {
                "1" isToken one thenState other
            }
            other state {
                "2" isToken two thenState default
            }
        }
        val result = lexer.tokenize("1212")
        assertEquals(
            listOf(
                LixyToken("1", 0, 1, one),
                LixyToken("2", 1, 2, two),
                LixyToken("1", 2, 3, one),
                LixyToken("2", 3, 4, two)
            ),
            result
        )
    }

    @Test
    fun `Lixy supports redirecting the default state`() {
        val a = stateLabel()
        val b = stateLabel()
        val ta = tokenType()
        val tb = tokenType()
        val lexer = lixy {
            default state a
            a state {
                "a" isToken ta thenState b
            }
            b state {
                "b" isToken tb thenState a
                "c" isToken tb thenState default // should also work
            }
        }
        val string = "abacaba"
        val expected = listOf(
            LixyToken("a", 0, 1, ta),
            LixyToken("b", 1, 2, tb),
            LixyToken("a", 2, 3, ta),
            LixyToken("c", 3, 4, tb),
            LixyToken("a", 4, 5, ta),
            LixyToken("b", 5, 6, tb),
            LixyToken("a", 6, 7, ta)
        )
        val actual = lexer.tokenize(string)
        assertEquals(expected, actual)
    }

    @Test
    fun `Lixy cannot redefine default after redirecting default`() {
        val a = stateLabel()
        val ta = tokenType()
        val b = stateLabel()
        val tb = tokenType()
        val exc = assertFailsWith<LixyException> {
            lixy {
                default state a
                default state {
                    "a" isToken ta
                }
                b state {
                    "b" isToken tb
                }
            }
        }
        assert(exc.message!!.contains("already defined"))
    }

    @Test
    fun `Lixy cannot redefine default state in single state lexer kind`() {
        val a = stateLabel()
        val ta = tokenType()
        val exc = assertFailsWith<LixyException> {
            lixy {
                state {
                    "a" isToken ta
                }
                default state a
            }
        }
        assert(exc.message!!.contains("Cannot redefine"))
        assert(exc.message!!.contains("single-state"))
    }
}