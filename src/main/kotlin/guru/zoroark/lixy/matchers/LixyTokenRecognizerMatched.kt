package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyStateLabel
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.NoState

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
     * The state that should be visited next, or [NoState] if no state should
     * be visited. `null` for the default state.
     */
    goesToState: LixyStateLabel? = NoState
) : LixyTokenMatcher(goesToState) {
    override fun match(s: String, startAt: Int): LixyToken? =
        recognizer.recognize(s, startAt)?.let { (recognizedSubstring, endsAt) ->
            LixyToken(recognizedSubstring, startAt, endsAt, tokenType)
        }

}
