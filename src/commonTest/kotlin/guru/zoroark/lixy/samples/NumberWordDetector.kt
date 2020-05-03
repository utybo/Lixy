package guru.zoroark.lixy.samples

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.matches
import guru.zoroark.lixy.matchers.repeated
import guru.zoroark.lixy.samples.TokenTypes.*
import kotlin.test.*

enum class TokenTypes : LixyTokenType {
    WORD, NUMBER, WHITESPACE
}

val lexer = lixy {
    state {
        matches("\\d+") isToken NUMBER
        matches("\\w+") isToken WORD
        anyOf(" ", "\n", "\t").repeated isToken WHITESPACE
    }
}

class WordNumberDetectorTest {
    @Test
    fun test_my_lexer() {
        val result = lexer.tokenize(
            """
            Hello 42 World
            I hope you are having a good time
            """.trimIndent()
        )
        result.forEach {
            println("${it.string} --> ${it.tokenType}")
        }
    }
}
