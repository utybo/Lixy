package guru.zoroark.lixy

import java.util.regex.Pattern

internal class RegexPatternRecognizer(val regexPattern: Pattern) : LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        val matcher = regexPattern.matcher(s.offsetBy(startAt))
        if(!matcher.lookingAt())
            return null
        return matcher.group() to matcher.end() + startAt
    }
}
