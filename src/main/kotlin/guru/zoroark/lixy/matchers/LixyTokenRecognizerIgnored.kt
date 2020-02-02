package guru.zoroark.lixy.matchers

/**
 * A type of matcher that ignores anything that matches the recognizer and
 * provides no result otherwise.
 */
class LixyTokenRecognizerIgnored(
    /**
     * The recognizer this matcher will use
     */
    val recognizer: LixyTokenRecognizer,
    /**
     * The behavior to follow for determining the next state
     */
    nextStateBehavior: LixyNextStateBehavior
) : LixyTokenMatcher(nextStateBehavior) {
    override fun match(s: String, startAt: Int): LixyMatcherResult {
        val (_, endsAt) = recognizer.recognize(s, startAt)
            ?: return LixyNoMatchResult
        return LixyIgnoreMatchResult(endsAt, nextStateBehavior)
    }
}
