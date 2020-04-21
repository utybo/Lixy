package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyDslStateEnvironment

/*
 * This version is less optimized than the JVM version due to limitations on the
 * Regex object, which does not have all of the features of the JVM's Matcher and
 * Pattern objects.
 */
internal class NonJvmRegexPatternRecognizer(private val regex: Regex) :
    LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? =
        regex.find(s, startAt)
            ?.takeIf { it.range.first == startAt }
            ?.let {
                it.value to it.range.last + 1
            }
}

actual fun LixyDslStateEnvironment.matches(pattern: String): LixyTokenRecognizer =
    NonJvmRegexPatternRecognizer(pattern.toRegex())