package guru.zoroark.lixy

class LixyDslEnvironment : Buildable<LixyLexer> {
    private val constructedStates: MutableList<StateDslEnvironment> = mutableListOf()

    fun state(body: StateDslEnvironment.() -> Unit) {
        constructedStates += StateDslEnvironment().apply(body)
    }

    override fun build(): LixyLexer {
        return LixyLexer(
            states = constructedStates.map { it.build() }
        )
    }
}
