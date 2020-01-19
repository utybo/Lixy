package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.offsetBy
import java.util.regex.Pattern

internal class RegexPatternRecognizer(val regexPattern: Pattern) :
    LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        val matcher = regexPattern.matcher(s).apply {
            region(startAt, s.length)
            // Look-behind and look-ahead can go beyond region bounds
            useTransparentBounds(true)
            // ^ and $ match the real start and end of the original string
            useAnchoringBounds(false)
        }
        if(!matcher.lookingAt())
            return null
        return matcher.group() to matcher.end()
    }
}
