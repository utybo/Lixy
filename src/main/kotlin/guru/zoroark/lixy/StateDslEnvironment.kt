package guru.zoroark.lixy

class StateDslEnvironment : Buildable<LixyState> {
    private val tokenMatchers = mutableListOf<LixyTokenMatcher>()

    override fun build(): LixyState {
        return LixyState(mutableListOf<LixyTokenMatcher>().apply {
            addAll(tokenMatchers)
        })
    }

    infix fun String.isToken(token: LixyTokenType) {
        tokenMatchers += LixyStringTokenMatcher(this, token)
    }

    operator fun LixyTokenMatcher.unaryPlus() {
        tokenMatchers += this
    }
}
