package guru.zoroark.lixy.matchers

/**
 * A token recognizer has the ability to detect a pattern within a string (the
 * exact pattern being entirely up to the recognizer) and then returns the
 * matched pattern as well as the ending index (that is, the index of the last
 * matched character + 1, this is considered to be an exclusive index).
 *
 * Recognizers cannot be used as-is within a lexer, as lexers expect
 * [matchers][LixyTokenMatcher]. To use a recognizer within a lexer, use the
 * [isToken][guru.zoroark.lixy.LixyDslStateEnvironment.isToken] method inside a
 * state block for a DSL approach, or a [LixyTokenRecognizerMatched] object.
 */
interface LixyTokenRecognizer {
    /**
     * Check whether the string [s] starting from index [startAt] matches
     * the pattern this recognizer uses.
     *
     * * If successful, return a pair with the matched pattern and the first
     * index that is outside of the matched pattern.
     * * If unsuccessful, return `null`
     */
    fun recognize(s: String, startAt: Int): Pair<String, Int>?
}
