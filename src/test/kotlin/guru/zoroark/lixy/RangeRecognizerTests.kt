package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.repeated
import guru.zoroark.lixy.matchers.toRecognizer
import kotlin.test.*

class RangeRecognizerTests {
    @Test
    fun `Range can be used as bare recognizer`() {
        val ta = tokenType()
        val tdigit = tokenType()
        val lexer = lixy {
            state {
                "a" isToken ta
                '0'..'9' isToken tdigit
            }
        }
        val string = "a1aa4a12345a67a890"
        val expected = listOf(
            LixyToken("a", 0, 1, ta),
            LixyToken("1", 1, 2, tdigit),
            LixyToken("a", 2, 3, ta),
            LixyToken("a", 3, 4, ta),
            LixyToken("4", 4, 5, tdigit),
            LixyToken("a", 5, 6, ta),
            LixyToken("1", 6, 7, tdigit),
            LixyToken("2", 7, 8, tdigit),
            LixyToken("3", 8, 9, tdigit),
            LixyToken("4", 9, 10, tdigit),
            LixyToken("5", 10, 11, tdigit),
            LixyToken("a", 11, 12, ta),
            LixyToken("6", 12, 13, tdigit),
            LixyToken("7", 13, 14, tdigit),
            LixyToken("a", 14, 15, ta),
            LixyToken("8", 15, 16, tdigit),
            LixyToken("9", 16, 17, tdigit),
            LixyToken("0", 17, 18, tdigit)
        )
        val actual = lexer.tokenize(string)
        assertEquals(expected, actual)
    }

    @Test
    fun `Range can be used with repetition`() {
        val top = tokenType()
        val tsign = tokenType()
        val tnumber = tokenType()
        val expectNumber = stateLabel()
        val expectOperation = stateLabel()
        val lexer = lixy {
            default state expectNumber
            expectNumber state {
                ('0'..'9').repeated isToken tnumber thenState expectOperation
                anyOf("+", "-") isToken tsign
                " ".repeated.ignore
            }
            expectOperation state {
                anyOf("+", "-", "*", "/") isToken top thenState expectNumber
                " ".repeated.ignore
            }
        }
        val string = "-136 + +287 * -35 / 42 + 9393913"
        val expected = listOf(
            LixyToken("-", 0, 1, tsign),
            LixyToken("136", 1, 4, tnumber),
            LixyToken("+", 5, 6, top),
            LixyToken("+", 7, 8, tsign),
            LixyToken("287", 8, 11, tnumber),
            LixyToken("*", 12, 13, top),
            LixyToken("-", 14, 15, tsign),
            LixyToken("35", 15, 17, tnumber),
            LixyToken("/", 18, 19, top),
            LixyToken("42", 20, 22, tnumber),
            LixyToken("+", 23, 24, top),
            LixyToken("9393913", 25, 32, tnumber)
        )
        val actual = lexer.tokenize(string)
        assertEquals(expected, actual)
    }
}