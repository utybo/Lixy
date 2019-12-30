package guru.zoroark.lixy

data class LixyLexer(val states: List<LixyState>) {
    val defaultState: LixyState
        get() = states[0]

    fun tokenize(s: String): List<LixyToken> {
        var index = 0
        val tokens = mutableListOf<LixyToken>()
        val state = defaultState
        while (index < s.length) {
            state.matchers.firstOrNull { matcher ->
                val match = matcher.match(s, index)
                if (match != null) {
                    if (match.startsAt < index) {
                        throw LixyException("Incoherent indices: matcher ${matcher.javaClass.simpleName} says the token starts at ${match.startsAt} when the current index is $index")
                    }
                    if (match.endsAt > s.length) {
                        throw LixyException("Incoherent indices: matcher ${matcher.javaClass.simpleName} says the token ends at ${match.endsAt}, which is out of bounds (total length is ${s.length})")
                    }
                    tokens.add(match)
                    index = match.endsAt
                    true
                }
                else {
                    false
                }
            } ?: throw LixyException("No match for string starting at index $index")
        }
        return tokens
    }
}
