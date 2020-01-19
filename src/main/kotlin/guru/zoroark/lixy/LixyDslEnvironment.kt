package guru.zoroark.lixy

/**
 * This class is used to build a [LixyLexer] using the Lixy DSL, and is used
 * in lambda-with-receivers to provide high level DSLs for the configuration of
 * a lexer.
 */
class LixyDslEnvironment : Buildable<LixyLexer> {
    /**
     * Indicates whether the lexer being built is stateful (multiple labeled
     * states with a default state, then stateful == true), stateless (single
     * unlabeled state, then stateful == false), or not determined yet (then
     * stateful == false)
     */
    private var stateful: Boolean? = null

    /**
     * States being constructed are stored here and are only actually
     * constructed when [build] is called.
     */
    private val constructedStates: MutableList<StateDslEnvironment> =
        mutableListOf()

    /**
     * Index of the default state in the [constructedStates] list.
     */
    private var defaultStateIndex: Int? = null

    /**
     * Create a state using a high-level DSL and add it to this object.
     * The state is not constructed immediately and is only constructed when
     * [build] is called.
     */
    fun state(body: StateDslEnvironment.() -> Unit) {
        if (stateful == true)
            throw LixyException("Cannot create an unlabeled state in a stateful context. You cannot mix labeled states and unlabeled states.")
        if (stateful == false)
            throw LixyException("Cannot create multiple unlabeled states. Try adding labels to your states, or using only one state.")
        stateful = false
        constructedStates += StateDslEnvironment().apply(body)
        defaultStateIndex = 0
    }

    /**
     * Utility class whose only role is to allow the default state construct.
     * (`default state { ... }`).
     */
    inner class StateInfixCreator internal constructor() {
        /**
         * Create a state.
         */
        infix fun state(body: StateDslEnvironment.() -> Unit) =
            this@LixyDslEnvironment.createLabeledState(null, body)
    }

    /**
     * Utility function that actually performs the addition of labeled states
     */
    private fun createLabeledState(
        label: LixyStateLabel?,
        body: StateDslEnvironment.() -> Unit
    ) {
        if (stateful == false)
            throw LixyException("Cannot create a labeled state in a single-state context. You cannot mix labeled states and unlabeled states.")
        if (label == null && defaultStateIndex != null)
            throw LixyException("Cannot create two default states. A null label implies that the state is the default state. Use a label for one of the default states or merge both states.")
        stateful = true
        constructedStates += StateDslEnvironment().apply(body)
        if (label == null)
            defaultStateIndex = constructedStates.size - 1
    }

    /**
     * DSL construct that allows you to directly create a default state.
     * Use it like this: `default state { ... }`
     *
     * The only use for this property is for its
     * [state][StateInfixCreator.state] function.
     */
    val default = StateInfixCreator()

    override fun build(): LixyLexer {
        return LixyLexer(
            states = constructedStates.map { it.build() }
        )
    }
}
