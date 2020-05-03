package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyDslStateEnvironment
import org.intellij.lang.annotations.Language
import java.util.regex.Pattern

internal class RegexPatternRecognizer(private val pattern: Pattern) :
    LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        val matcher = pattern.matcher(s).apply {
            region(startAt, s.length)
            // Look-behind and look-ahead can go beyond region bounds
            useTransparentBounds(true)
            // ^ and $ match the real start and end of the original string
            useAnchoringBounds(false)
        }
        if (!matcher.lookingAt())
            return null
        return matcher.group() to matcher.end()
    }
}

actual fun LixyDslStateEnvironment.matches(@Language("RegExp") pattern: String): LixyTokenRecognizer =
    RegexPatternRecognizer(Pattern.compile(pattern))