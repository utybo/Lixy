package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*

abstract class LixyDslGenericMatcherEnvironment(
    /**
     * The original recognizer that should be used by the matcher that will be
     * built
     */
    var baseRecognizer: LixyTokenRecognizer
): Buildable<LixyTokenMatcher> {
    /**
     * Which state the built matcher will lead to, in the form of a
     * [LixyNextStateBehavior] object.
     *
     * @see thenState
     * @see LixyNextStateBehavior
     */
    protected var nextStateBehavior: LixyNextStateBehavior = LixyNoStateChange

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
}