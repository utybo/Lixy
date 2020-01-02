package guru.zoroark.lixy

/**
 * This class is used to build a [LixyLexer] using the Lixy DSL, and is used
 * in lambda-with-receivers to provide high level DSLs for the configuration of
 * a lexer.
 */
class LixyDslEnvironment : Buildable<LixyLexer> {
    /**
     * States being constructed are stored here and are only actually
     * constructed when [build] is called.
     */
    private val constructedStates: MutableList<StateDslEnvironment> = mutableListOf()

    /**
     * Create a state using a high-level DSL and add it to this object.
     * The state is not constructed immediately and is only constructed when
     * [build] is called.
     */
    fun state(body: StateDslEnvironment.() -> Unit) {
        constructedStates += StateDslEnvironment().apply(body)
    }

    override fun build(): LixyLexer {
        return LixyLexer(
            states = constructedStates.map { it.build() }
        )
    }
}
