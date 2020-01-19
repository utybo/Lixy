package guru.zoroark.lixy

import kotlin.test.*

class LixyTest {

    @Test
    fun `Empty Lixy should crash`() {
        // I mean, yeah, that lexer is not going to do anything
        assertFailsWith<LixyException> {
            lixy {}
        }
    }

    @Test
    fun `Lixy constructs single unlabeled state`() {
        // Should construct a single empty state
        val ret = lixy {
            state {}
        }

        assertEquals(ret.statesCount, 1)
        assert(ret.defaultState.matchers.isEmpty())
    }

    @Test
    fun `Lixy is able to lex simple unlabeled state`() {
        // Should construct a single state with a single matcher
        val simpleStateDot =
            tokenType()
        val lexer = lixy {
            state {
                "." isToken simpleStateDot
            }
        }
        val tokens = lexer.tokenize("....")
        assertEquals(lexer.statesCount, 1)
        assertEquals(lexer.defaultState.matchers.size, 1)
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
        // Should successfully lex with a single more complex state
        val ttdot = tokenType()
        val ttspace = tokenType()
        val tthello = tokenType()
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
        // Additional testing, specifically because lexing tests are supposed
        // to be done sequentially (i.e. check for the first pattern, then the
        // second, etc.)
        val tttriple = tokenType()
        val ttpair = tokenType()
        val ttsingle = tokenType()
        val ttspace = tokenType()
        val lexer = lixy {
            state {
                "..." isToken tttriple
                ".." isToken ttpair
                "." isToken ttsingle
                " " isToken ttspace
            }
        }
        //                           111223445666789
        val tokens = lexer.tokenize("..... .. .... .")
        assertEquals(
            tokens,
            listOf(
                LixyToken("...", 0, 3, tttriple), // 1
                LixyToken("..", 3, 5, ttpair), // 2
                LixyToken(" ", 5, 6, ttspace), // 3
                LixyToken("..", 6, 8, ttpair), // 4
                LixyToken(" ", 8, 9, ttspace), // 5
                LixyToken("...", 9, 12, tttriple), // 6
                LixyToken(".", 12, 13, ttsingle), // 7
                LixyToken(" ", 13, 14, ttspace), // 8
                LixyToken(".", 14, 15, ttsingle) // 9
            )
        )
    }

    @Test
    fun `Lixy supports custom matchers`() {
        // ttype will be the type returned by our custom matcher
        val ttype = tokenType()
        val ttdot = tokenType()
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
        // Our token types
        val ttype = tokenType()
        val ttdot = tokenType()
        val lexer = lixy {
            state {
                // Erroneous matcher
                +matcher { s, start ->
                    if (start == 1)
                    // The second character returns a token that starts
                    // on the very first character, which is a big no-no
                        LixyToken(s[0].toString(), 0, 2, ttype)
                    else
                    // Returning null to signal no match
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
        val ttype = tokenType()
        val ttdot = tokenType()
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
        val ttdot = tokenType()
        val lexer = lixy {
            state {
                "." isToken ttdot
            }
        }
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize("a")
        }
    }

    @Test
    fun `Lixy supports regex`() {
        val ttregex = tokenType()
        val lexer = lixy {
            state {
                matches("(abc){2}") isToken ttregex
            }
        }
        val result = lexer.tokenize("abcabcabcabc")
        assertEquals(
            listOf(
                LixyToken("abcabc", 0, 6, ttregex),
                LixyToken("abcabc", 6, 12, ttregex)
            ),
            result
        )
    }

    @Test
    fun `Lixy supports transparent look-behind in regex`() {
        val ttregex = tokenType()
        val ttype = tokenType()
        val lexer = lixy {
            state {
                "a" isToken ttype
                matches("(?<=a)b") isToken ttregex
                "b" isToken ttype
            }
        }
        val result = lexer.tokenize("abb")
        assertEquals(
            listOf(
                LixyToken("a", 0, 1, ttype),
                LixyToken("b", 1, 2, ttregex),
                LixyToken("b", 2, 3, ttype)
            ),
            result
        )
    }

    @Test
    fun `Lixy regex matches start and end of string as real start and end`() {
        val ttregex = tokenType()
        val ttype = tokenType()
        val lexer = lixy {
            state {
                matches("^a") isToken ttregex
                "a" isToken ttype
                matches("b$") isToken ttregex
                "b" isToken ttype
            }
        }
        val result = lexer.tokenize("aabb")
        assertEquals(
            listOf(
                LixyToken("a", 0, 1, ttregex),
                LixyToken("a", 1, 2, ttype),
                LixyToken("b", 2, 3, ttype),
                LixyToken("b", 3, 4, ttregex)
            ),
            result
        )
    }

    @Test
    fun `Lixy anyOf crashes if no provided arguments`() {
        val tokenType =
            tokenType()
        val exc = assertFailsWith<LixyException> {
            lixy {
                state {
                    anyOf() isToken tokenType
                }
            }
        }
        assertNotNull(exc.message)
        assert(exc.message!!.contains("anyOf") && exc.message!!.contains("at least one"))
    }

    @Test
    fun `Lixy supports anyOf multistring matcher`() {
        val basicTokenType =
            tokenType()
        val multiTokenType =
            tokenType()
        val lexer = lixy {
            state {
                " " isToken basicTokenType
                anyOf("a", "b", "d", "z") isToken multiTokenType
            }
        }
        val tokens = lexer.tokenize("a bz d bdz")
        assertEquals(
            listOf(
                LixyToken("a", 0, 1, multiTokenType),
                LixyToken(" ", 1, 2, basicTokenType),
                LixyToken("b", 2, 3, multiTokenType),
                LixyToken("z", 3, 4, multiTokenType),
                LixyToken(" ", 4, 5, basicTokenType),
                LixyToken("d", 5, 6, multiTokenType),
                LixyToken(" ", 6, 7, basicTokenType),
                LixyToken("b", 7, 8, multiTokenType),
                LixyToken("d", 8, 9, multiTokenType),
                LixyToken("z", 9, 10, multiTokenType)
            ),
            tokens
        )
    }
}
