package guru.zoroark.lixy.samples

import guru.zoroark.lixy.LixyStateLabel
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.samples.StringDetectorLabels.*
import guru.zoroark.lixy.samples.StringDetectorTypes.*
import kotlin.test.*

enum class StringDetectorTypes: LixyTokenType {
    word, whitespace, quotes, stringContent, punctuation
}

enum class StringDetectorLabels: LixyStateLabel {
    inString
}

class StringDetector {
    @Test
    fun `String detector`() {
        val lexer = lixy {
            default state {
                anyOf(" ", "\t", "\n") isToken whitespace
                "\"" isToken quotes thenState inString
                anyOf(".", ",", "!", "?") isToken punctuation
                matches("\\w+") isToken word
            }
            inString state {
                matches("""(\\"|[^"])+""") isToken stringContent
                "\"" isToken quotes thenState default
            }
        }
        val str = """
            Hello Kotlin! "This is \" Pretty Cool" Heyo!
        """.trimIndent()
        val tokens = lexer.tokenize(str)
        tokens.forEach { println(it.string + " --> " + it.tokenType) }
    }
}