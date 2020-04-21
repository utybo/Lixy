package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyException

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


/**
 * Universal function to turn an object into a recognizer. This function
 * returns a [token recognizer][LixyTokenRecognizer].
 *
 * [x] may be:
 *
 * * A [String], in which case the returned recognizer will attempt to match the
 * string exactly. (Pseudo-recognizer)
 *
 * * A [CharRange], in which case the recognizer will attempt to match a single
 * character from the given range. (Pseudo-recognizer)
 *
 * * A [recognizer][LixyTokenRecognizer], in which case [x] will be returned
 * directly. This is useful for having one-fits-all methods that can accommodate
 * both directly taking in a recognizer and a pseudo-recognizer. (Recognizer)
 *
 * If [x] does not match any of the previous types, a [LixyException] is thrown.
 *
 * @param x An object, which can be either a [String], a [CharRange], or a
 * [LixyTokenRecognizer]
 *
 * @return A recognizer that matches the behaviors stated in the description of
 * this function.
 */
fun toRecognizer(x: Any): LixyTokenRecognizer =
    when (x) {
        is LixyTokenRecognizer -> x
        is String -> LixyStringTokenRecognizer(x)
        is CharRange -> LixyCharRangeTokenRecognizer(x)
        else -> throw LixyException(
            "Unable to convert ${x::class.simpleName} to a recognizer."
        )
    }