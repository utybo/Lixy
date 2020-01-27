package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*

/**
 * A simple builder for matchers, whose main purpose is to provide a way to
 * select a "next state" for a matcher through the [thenState] function.
 */
class LixyDslMatcherEnvironment(
    /**
     * The original recognizer that should be used by the matcher that will be
     * built
     */
    var baseRecognizer: LixyTokenRecognizer,
    /**
     * The token type this matcher will be matched against.
     */
    var matchesToTokenType: LixyTokenType
) : Buildable<LixyTokenMatcher> {
    /**
     * Which state the built matcher will lead to, in the form of a
     * [LixyNextStateBehavior] object.
     *
     * @see thenState
     * @see LixyNextStateBehavior
     */
    private var nextStateBehavior: LixyNextStateBehavior = LixyNoStateChange

    /**
     * Specifies that, once a match is found, the lexer should use the given
     * state [next]. Valid values are:
     *
     * * [default][LixyDslEnvironment.default] or `null` to go to the default
     *   state
     * * A [LixyStateLabel] to go to the state with the given label
     */
    infix fun thenState(next: LixyStateLabel) {
        nextStateBehavior = LixyGoToLabeledState(next)
    }

    infix fun thenState(
        @Suppress("UNUSED_PARAMETER")
        defaultMarker: LixyDslEnvironment.StateInfixCreator
    ) {
        nextStateBehavior = LixyGoToDefaultState
    }

    override fun build(): LixyTokenMatcher =
        LixyTokenRecognizerMatched(
            baseRecognizer,
            matchesToTokenType,
            nextStateBehavior
        )
}