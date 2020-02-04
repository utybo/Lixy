package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.repeated
import kotlin.test.*

class RepeatedRecognizerTest {

    @Test
    fun `Repeated string recognizer test`() {
        val thi = tokenType()
        val thello = tokenType()
        val lexer = lixy {
            state {
                "hi ".repeated isToken thi
                "hello" isToken thello
                " ".ignore
            }
        }
        val string = "hi hi hi hi hi hi hello hi hi hello hello"
        val expected = listOf(
            LixyToken("hi hi hi hi hi hi ", 0, 18, thi),
            LixyToken("hello", 18, 23, thello),
            LixyToken("hi hi ", 24, 30, thi),
            LixyToken("hello", 30, 35, thello),
            LixyToken("hello", 36, 41, thello)
        )
        val actual = lexer.tokenize(string)
        assertEquals(expected, actual)
    }

    @Test
    fun `Repeated string recognizer with additional parameters test`() {
        val thi = tokenType()
        val thello = tokenType()
        val lexer = lixy {
            state {
                "hi ".repeated(min = 3, max = 5) isToken thi
                "hello" isToken thello
                " ".ignore
            }
        }
        val stringFailNotEnough = "hello hi hi hello"
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize(stringFailNotEnough)
        }
        val stringFailTooMany = "hi hi hi hi hi hi hello"
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize(stringFailTooMany)
        }
        val stringSuccess =
            "hellohi hi hi hello hi hi hi hi hello hello hi hi hi hi hi hello hi hi hi hi hi "
        val expected = listOf(
            LixyToken("hello", 0, 5, thello),
            LixyToken("hi hi hi ", 5, 14, thi),
            LixyToken("hello", 14, 19, thello),
            LixyToken("hi hi hi hi ", 20, 32, thi),
            LixyToken("hello", 32, 37, thello),
            LixyToken("hello", 38, 43, thello),
            LixyToken("hi hi hi hi hi ", 44, 59, thi),
            LixyToken("hello", 59, 64, thello),
            LixyToken("hi hi hi hi hi ", 65, 80, thi)
        )
        val result = lexer.tokenize(stringSuccess)
        assertEquals(expected, result)
    }
}