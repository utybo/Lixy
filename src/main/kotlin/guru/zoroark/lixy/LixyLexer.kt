package guru.zoroark.lixy

/**
 * A LixyLexer is a data class that contains the states that will be used for
 * the lexing process. It is the main entry for using a constructed lexer.
 *
 * The class itself only contains states and not much else.
 *
 * The function [tokenize] can be used to use the lexer on a string
 */
data class LixyLexer(val states: Map<LixyStateLabel?, LixyState>) {
    /**
     * The default state in the case of a multi labeled state lexer, or the
     * only state of the lexer in the case of a single unlabeled state lexer
     */
    val defaultState: LixyState
        get() = states[null] ?: error("No default state in lexer")

    /**
     * The tokenize method will turn a string into a list of tokens based on
     * the [LixyState]s contained in this [LixyLexer] ([states]) and the
     */
    fun tokenize(s: String): List<LixyToken> {
        var index = 0
        val tokens = mutableListOf<LixyToken>()
        val state = defaultState
        // While we are in the string
        while (index < s.length) {
            // The first matcher to match will return true, and firstOrNull
            // returns null if no matchers matched, in which case we throw an
            // exception
            state.matchers.firstOrNull { matcher ->
                // Attempt to match
                matcher.match(s, index)?.let { match ->
                    when {
                        match.string.length > match.endsAt - match.startsAt ->
                            throw LixyException("Returned token string (${match.string}) is too large for the given range (${match.startsAt}-${match.endsAt})")
                        match.startsAt < index ->
                            throw LixyException("Incoherent indices: matcher ${matcher.javaClass.simpleName} says the token starts at ${match.startsAt} when the current index is $index")
                        match.endsAt > s.length ->
                            throw LixyException("Incoherent indices: matcher ${matcher.javaClass.simpleName} says the token ends at ${match.endsAt}, which is out of bounds (total length is ${s.length})")
                        else -> {
                            tokens.add(match)
                            index = match.endsAt
                            true
                        }
                    }
                } ?: false
            }
            // firstOrNull returned null: throw an exception, nothing
            // matched
                ?: throw LixyNoMatchException("No match for string starting at index $index")
        }
        return tokens
    }
}
