package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*

/**
 * A simple builder for matchers, whose main purpose is to provide a way to
 * select a "next state" for a matcher through the [thenState] function.
 */
class LixyDslMatchedMatcherEnvironment(
    /**
     * The original recognizer that should be used by the matcher that will be
     * built
     */
    baseRecognizer: LixyTokenRecognizer,
    /**
     * The token type this matcher will be matched against.
     */
    var matchesToTokenType: LixyTokenType
) : LixyDslGenericMatcherEnvironment(baseRecognizer) {
    override fun build(): LixyTokenMatcher =
        LixyTokenRecognizerMatched(
            baseRecognizer,
            matchesToTokenType,
            nextStateBehavior
        )
}