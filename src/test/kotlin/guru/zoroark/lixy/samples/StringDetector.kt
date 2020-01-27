package guru.zoroark.lixy.samples

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.samples.StringDetectorLabels.*
import guru.zoroark.lixy.samples.StringDetectorTypes.*
import kotlin.test.*

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