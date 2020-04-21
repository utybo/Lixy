package guru.zoroark.lixy.matchers

/**
 * Implementation of a [LixyTokenRecognizer] that attempts to recognize a
 * given string exactly. It will return a pair with the matched string and the
 * ending index (exclusive) if recognized.
 */
class LixyStringTokenRecognizer(
    /**
     * The string that should be recognized by this
     * [LixyStringTokenRecognizer]
     */
    val toRecognize: String
) : LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        if (startAt + toRecognize.length > s.length) {
            return null // Cannot match (match goes beyond s boundaries)
        }
        for (i in toRecognize.indices) {
            if (s[i + startAt] != toRecognize[i])
                return null // Does not match
        }
        // Everything matched, return a token
        return s.substring(
            startAt,
            startAt + toRecognize.length
        ) to startAt + toRecognize.length

    }

}
