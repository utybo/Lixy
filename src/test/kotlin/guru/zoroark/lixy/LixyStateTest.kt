package guru.zoroark.lixy

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
}