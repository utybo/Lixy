package guru.zoroark.lixy

/**
 * A token matcher is an object that can determine whether a string at a given
 * offset matches some pattern. The matcher then returns a corresponding token
 * or null if no match is found.
 */
interface LixyTokenMatcher {
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
    fun match(s: String, startAt: Int): LixyToken?
}
