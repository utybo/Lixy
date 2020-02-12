package guru.zoroark.lixy

/**
 * This class is used to build a [LixyLexer] using the Lixy DSL, and is used
 * in lambda-with-receivers to provide high level DSLs for the configuration of
 * a lexer.
 */
class LixyDslEnvironment : Buildable<LixyLexer> {
    /**
     * An enum that represents the different kinds of lexers that can be built
     */
    private enum class Kind {
        /**
         * A lexer that uses exactly one default state and zero or more other
         * labeled states
         */
        LABELED_STATES,
        /**
         * A lexer that uses exactly one unlabeled state
         */
        SINGLE_STATE,
        /**
         * Used when the kind of lexer is not known yet
         */
        UNDETERMINED
    }

    /**
     * Indicates whether the kind of lexer that is being built. Refer to [Kind]'s
     * KDoc for more details
     */
    private var lexerKind: Kind = Kind.UNDETERMINED

    /**
     * States being constructed are stored here and are only actually
     * constructed when [build] is called.
     */
    private val constructedStates: MutableMap<LixyStateLabel?, LixyDslStateEnvironment> =
        mutableMapOf()

    /**
     * Create a state using a high-level DSL and add it to this object.
     * The state is not constructed immediately and is only constructed when
     * [build] is called.
     */
    fun state(body: LixyDslStateEnvironment.() -> Unit): Unit =
        when (lexerKind) {
            Kind.LABELED_STATES ->
                throw LixyException("Cannot create an unlabeled state in a stateful context. You cannot mix labeled states and unlabeled states.")
            Kind.SINGLE_STATE ->
                throw LixyException("Cannot create multiple unlabeled states. Try adding labels to your states, or using only one state.")
            Kind.UNDETERMINED -> {
                lexerKind = Kind.SINGLE_STATE
                constructedStates[null] = LixyDslStateEnvironment().apply(body)
            }
        }

    infix fun LixyStateLabel.state(body: LixyDslStateEnvironment.() -> Unit): Unit =
        createLabeledState(this, body)

    /**
     * Utility class whose only role is to allow the default state construct.
     * (`default state { ... }`).
     */
    inner class StateInfixCreator internal constructor() {
        /**
         * Create a state.
         */
        infix fun state(body: LixyDslStateEnvironment.() -> Unit) =
            this@LixyDslEnvironment.createLabeledState(null, body)
    }

    /**
     * DSL construct that allows you to directly create a default state.
     * Use it like this: `default state { ... }`
     *
     * The only use for this property is for its
     * [state][StateInfixCreator.state] function.
     */
    val default = StateInfixCreator()

    /**
     * Utility function that actually performs the addition of labeled states
     */
    private fun createLabeledState(
        label: LixyStateLabel?,
        body: LixyDslStateEnvironment.() -> Unit
    ): Unit = when {
        lexerKind == Kind.SINGLE_STATE ->
            throw LixyException("Cannot create a labeled state in a single-state context. You cannot mix labeled states and unlabeled states.")
        label == null && constructedStates.containsKey(null) ->
            throw LixyException("Cannot create two default states. A null label implies that the state is the default state. Use a label for one of the default states or merge both states.")
        constructedStates.containsKey(label) ->
            throw LixyException("Cannot create two states with the same label. Use a different label so that all states have distinct labels.")
        else -> {
            lexerKind = Kind.LABELED_STATES
            constructedStates[label] = LixyDslStateEnvironment().apply(body)
        }
    }

    override fun build(): LixyLexer {
        return LixyLexer(
            states = constructedStates.mapValues { (_, v) -> v.build() }
        )
    }
}
