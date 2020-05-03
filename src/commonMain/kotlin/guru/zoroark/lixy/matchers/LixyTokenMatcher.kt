package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyToken

/**
 * A token matcher is an object that can determine whether a string at a given
 * offset matches some pattern. The matcher then returns a corresponding token
 * or null if no match is found.
 */
abstract class LixyTokenMatcher(
    /**
     * The behavior for choosing a next state that will be followed upon a
     * successful match.
     *
     * @see LixyNextStateBehavior
     */
    protected val nextStateBehavior: LixyNextStateBehavior = LixyNoStateChange
) {
    /**
     * This function determines whether the string [s], starting at the index
     * [startAt] (inclusive), matches some pattern. The exact pattern is
     * dependent on the implementation.
     *
     * @param s The string to analyze
     * @param startAt The index after which the string must be considered
     * @return Null if no match is possible, or a [LixyToken] that corresponds
     * to the matched substring.
     */
    abstract fun match(s: String, startAt: Int): LixyMatcherResult
}
