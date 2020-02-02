package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*

/**
 * A LixyLexer is a data class that contains the states that will be used for
 * the lexing process. It is the main entry for using a constructed lexer.
 *
 * The class itself only contains states and not much else.
 *
 * The function [tokenize] can be used to use the lexer on a string
 */
data class LixyLexer(private val states: Map<LixyStateLabel?, LixyState>) {
    /**
     * The default state in the case of a multi labeled state lexer, or the
     * only state of the lexer in the case of a single unlabeled state lexer
     */
    val defaultState: LixyState
        get() = states[null] ?: error("No default state in lexer")

    /**
     * The amount of states contained within this lexer
     */
    val statesCount: Int
        get() = states.size

    /**
     * The tokenize method will turn a string into a list of tokens based on
     * the [LixyState]s contained in this [LixyLexer] ([states]) and the
     */
    fun tokenize(s: String): List<LixyToken> {
        var index = 0
        val tokens = mutableListOf<LixyToken>()
        var state = defaultState
        /**
         * A function for updating the lexer's index and state based on what
         * is returned by a matcher
         */
        fun updateParams(newIndex: Int, behavior: LixyNextStateBehavior) {
            index = newIndex
            state = when (behavior) {
                is LixyGoToDefaultState -> defaultState
                is LixyGoToLabeledState -> getState(behavior.stateLabel)
                is LixyNoStateChange -> state
            }
        }
        // While we are in the string
        while (index < s.length) {
            // The first matcher to match will return true, and firstOrNull
            // returns null if no matchers matched, in which case we throw an
            // exception
            state.matchers.firstOrNull { matcher ->
                // Attempt to match
                when (val result = matcher.match(s, index)) {
                    // Did not match
                    is LixyNoMatchResult -> false
                    // Matched, but no token should be created (ignore the
                    // match) in the final token sequence
                    is LixyIgnoreMatchResult -> with(result) {
                        updateParams(tokenEndsAt, nextStateBehavior)
                        true
                    }
                    // Matched,
                    is LixyMatchedTokenResult -> result.token.let {
                        checkTokenBounds(it, index, s.length)
                        tokens += it
                        updateParams(it.endsAt, result.nextStateBehavior)
                        true
                    }
                }
            }
            // firstOrNull returned null: throw an exception, nothing matched
                ?: throw LixyNoMatchException("No match for string starting at index $index")
        }
        return tokens
    }

    private fun checkTokenBounds(
        match: LixyToken,
        index: Int,
        totalLength: Int
    ): Unit = with(match) {
        when {
            string.length > endsAt - startsAt ->
                throw LixyException("Returned token string ($string) is too large for the given range ($startsAt-$endsAt)")
            startsAt < index ->
                throw LixyException("Incoherent indices: matcher says the token starts at $startsAt when the current index is $index")
            endsAt > totalLength ->
                throw LixyException("Incoherent indices: matcher says the token ends at $endsAt, which is out of bounds (total length is $totalLength)")
        }
    }

    /**
     * Get the state with the given label
     *
     * @throws LixyException if the state was not found
     */
    fun getState(label: LixyStateLabel?): LixyState =
        states[label] ?: throw LixyException("State with given label not found")
}
