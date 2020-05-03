package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType

/**
 * This class can be used to associate a [LixyTokenRecognizer] with a token type,
 * forming a complete [LixyTokenMatcher]
 */
class LixyTokenRecognizerMatched(
    /**
     * The recognizer this matcher will use
     */
    val recognizer: LixyTokenRecognizer,
    /**
     * The token type to match recognized tokens to
     */
    val tokenType: LixyTokenType,
    /**
     * The behavior to follow for determining the next state
     */
    nextStateBehavior: LixyNextStateBehavior = LixyNoStateChange
) : LixyTokenMatcher(nextStateBehavior) {
    override fun match(s: String, startAt: Int): LixyMatcherResult =
        recognizer.recognize(s, startAt)?.let { (recognizedSubstring, endsAt) ->
            LixyMatchedTokenResult(
                LixyToken(recognizedSubstring, startAt, endsAt, tokenType),
                nextStateBehavior
            )
        } ?: LixyNoMatchResult

}
