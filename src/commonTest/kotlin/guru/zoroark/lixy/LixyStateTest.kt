package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.LixyTokenRecognizerMatched
import guru.zoroark.lixy.matchers.LixyStringTokenRecognizer
import kotlin.test.*

class LixyStateTest {
    @Test
    fun lixy_supports_constructing_one_default_state() {
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
    fun lixy_fails_to_construct_unlabeled_then_labeled_default() {
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
    fun lixy_fails_to_construct_labeled_default_then_unlabeled() {
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
    fun lixy_fails_to_construct_multiple_unlabeled_states() {
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
    fun lixy_fails_to_construct_multiple_default_states() {
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
    fun lixy_cannot_create_two_states_with_the_same_label() {
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
        assertTrue(exc.message!!.contains("two states with the same label"))
    }

    @Test
    fun lixy_successfully_constructs_multiple_states_and_starts_on_default() {
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
    fun lixy_supports_switching_from_state_to_state() {
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
    fun lixy_supports_redirecting_the_default_state() {
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
    fun lixy_cannot_redefine_default_after_redirecting_default() {
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
        assertTrue(exc.message!!.contains("already defined"))
    }

    @Test
    fun lixy_cannot_redefine_default_state_in_single_state_lexer_kind() {
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
        assertTrue(exc.message!!.contains("Cannot redefine"))
        assertTrue(exc.message!!.contains("single-state"))
    }
}