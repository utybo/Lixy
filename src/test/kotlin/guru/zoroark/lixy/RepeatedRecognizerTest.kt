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

    @Test
    fun `Any repeated recognizer test`() {
        val tgreet = tokenType()
        val ttest = tokenType()
        val lexer = lixy {
            state {
                anyOf("hello", "hi", "hey").repeated isToken tgreet
                "test" isToken ttest
                " ".ignore
            }
        }
        val string = "hellohello hihihi hey hihi test heyheyhey"
        val expected = listOf(
            LixyToken("hellohello", 0, 10, tgreet),
            LixyToken("hihihi", 11, 17, tgreet),
            LixyToken("hey", 18, 21, tgreet),
            LixyToken("hihi", 22, 26, tgreet),
            LixyToken("test", 27, 31, ttest),
            LixyToken("heyheyhey", 32, 41, tgreet)
        )
        val result = lexer.tokenize(string)
        assertEquals(expected, result)
    }

    @Test
    fun `Any repeated recognizer with additional parameters test`() {
        val tgreet = tokenType()
        val ttest = tokenType()
        val lexer = lixy {
            state {
                anyOf("hello", "hi", "hey").repeated(min = 2, max = 4) isToken tgreet
                "test" isToken ttest
                " ".ignore
            }
        }
        val stringNotEnough = "hellohello hihihi hey hihihihi"
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize(stringNotEnough)
        }
        val stringTooMany = "hellohellohello heyhey hihihihihi hihihi"
        assertFailsWith<LixyNoMatchException> {
            lexer.tokenize(stringTooMany)
        }
        val stringSuccess = "hellohello hihihi hihi test heyheyheyhey"
        val expected = listOf(
            LixyToken("hellohello", 0, 10, tgreet),
            LixyToken("hihihi", 11, 17, tgreet),
            LixyToken("hihi", 18, 22, tgreet),
            LixyToken("test", 23, 27, ttest),
            LixyToken("heyheyheyhey", 28, 40, tgreet)
        )
        val result = lexer.tokenize(stringSuccess)
        assertEquals(expected, result)
    }
}