package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyStateLabel
import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.NoState

/**
 * A token matcher is an object that can determine whether a string at a given
 * offset matches some pattern. The matcher then returns a corresponding token
 * or null if no match is found.
 */
abstract class LixyTokenMatcher(
    /**
     * The next state that the lexer should go to after a successful match.
     * `null` for the default state, [NoState] if the state should not be
     * changed.
     */
    val goesToState: LixyStateLabel? = NoState
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
    abstract fun match(s: String, startAt: Int): LixyToken?
}
