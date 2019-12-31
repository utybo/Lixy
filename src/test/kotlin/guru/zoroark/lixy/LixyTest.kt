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

    @Test
    fun `Lixy is able to lex multiple token types unlabeled state`() {
        val ttdot = LixyTokenType()
        val ttspace = LixyTokenType()
        val tthello = LixyTokenType()
        val lexer = lixy {
            state {
                "." isToken ttdot
                " " isToken ttspace
                "hello" isToken tthello
            }
        }
        val tokens = lexer.tokenize("hello hello. hello..  ")
        assertEquals(
            tokens,
            listOf(
                LixyToken("hello", 0, 5, tthello),
                LixyToken(" ", 5, 6, ttspace),
                LixyToken("hello", 6, 11, tthello),
                LixyToken(".", 11, 12, ttdot),
                LixyToken(" ", 12, 13, ttspace),
                LixyToken("hello", 13, 18, tthello),
                LixyToken(".", 18, 19, ttdot),
                LixyToken(".", 19, 20, ttdot),
                LixyToken(" ", 20, 21, ttspace),
                LixyToken(" ", 21, 22, ttspace)
            )
        )
    }

    @Test
    fun `Lixy is able to parse some funny string patterns, v2`() {
        val tttriple = LixyTokenType()
        val ttpair = LixyTokenType()
        val ttsingle = LixyTokenType()
        val ttspace = LixyTokenType()
        val lexer = lixy {
            state {
                "..." isToken tttriple
                ".." isToken ttpair
                "." isToken ttsingle
                " " isToken ttspace
            }
        }

        val tokens = lexer.tokenize("..... .. .... .")
        assertEquals(
            tokens,
            listOf(
                LixyToken("...", 0, 3, tttriple),
                LixyToken("..", 3, 5, ttpair),
                LixyToken(" ", 5, 6, ttspace),
                LixyToken("..", 6, 8, ttpair),
                LixyToken(" ", 8, 9, ttspace),
                LixyToken("...", 9, 12, tttriple),
                LixyToken(".", 12, 13, ttsingle),
                LixyToken(" ", 13, 14, ttspace),
                LixyToken(".", 14, 15, ttsingle)
            )
        )
    }

    @Test
    fun `Lixy supports custom matchers`() {
        val ttype = LixyTokenType()
        val ttdot = LixyTokenType()
        val customMatcher = matcher { s, i ->
            if (s[i] == 'e') {
                LixyToken("e", i, i + 1, ttype)
            } else {
                null
            }
        }
        val lexer = lixy {
            state {
                +customMatcher
                "." isToken ttdot
            }
        }

        val tokens = lexer.tokenize(".e..ee")
        assertEquals(
            tokens,
            listOf(
                LixyToken(".", 0, 1, ttdot),
                LixyToken("e", 1, 2, ttype),
                LixyToken(".", 2, 3, ttdot),
                LixyToken(".", 3, 4, ttdot),
                LixyToken("e", 4, 5, ttype),
                LixyToken("e", 5, 6, ttype)
            )
        )
    }

    @Test
    fun `Lixy incoherent matcher results cause exception (start before index)`() {
        val ttype = LixyTokenType()
        val ttdot = LixyTokenType()
        val lexer = lixy {
            state {
                +matcher { s, start ->
                    if (start == 1)
                        LixyToken(s[0].toString(), 0, 2, ttype)
                    else
                        null
                }
                "." isToken ttdot
            }
        }
        val exc = assertFailsWith<LixyException> {
            lexer.tokenize("...")
        }
        assertNotNull(exc.message)
        assert(exc.message!!.contains("token starts"))
    }

    @Test
    fun `Lixy incoherent matcher results cause exception (end is too far)`() {
        val ttype = LixyTokenType()
        val ttdot = LixyTokenType()
        val lexer = lixy {
            state {
                +matcher { s, start ->
                    if (start == s.length - 1) {
                        LixyToken(
                            s[start].toString(),
                            s.length - 1,
                            s.length + 1,
                            ttype
                        )
                    } else {
                        null
                    }
                }
                "." isToken ttdot
            }
        }
        val exc = assertFailsWith<LixyException> {
            lexer.tokenize("....")
        }
        assertNotNull(exc.message)
        assert(exc.message!!.contains("token ends"))
    }

    @Test
    fun `Lixy no match fails`() {
        val ttdot = LixyTokenType()
        val lexer = lixy {
            state {
                "." isToken ttdot
            }
        }
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize("a")
        }
    }
}
