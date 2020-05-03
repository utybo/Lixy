package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*

/**
 * This classed is used to build a lexer state ([LixyState]) using a DSL, and is
 * used inside a lambda-with receiver. It also defines high level functions for
 * constructing matchers, such as [anyOf] or [matches], and using custom
 * matchers with the [+ operator][unaryPlus].
 *
 * States themselves are only constructed when calling [build], since
 * [LixyState] objects are immutable and the goal of the DSL is to provide a way
 * to configure them.
 */
class LixyDslStateEnvironment : Buildable<LixyState> {
    private val tokenMatchers = mutableListOf<Buildable<LixyTokenMatcher>>()

    override fun build(): LixyState {
        return LixyState(mutableListOf<LixyTokenMatcher>().apply {
            addAll(tokenMatchers.map { it.build() })
        })
    }

    /**
     * Add a matcher to this state that matches the recognizer (or
     * pseudo-recognizer) on the left to the token type on the right.
     *
     * The recognizer (the object on the left on which you are calling
     * `isToken`) may be any of the types supported by [toRecognizer].
     *
     * @param token The token to associate `this` to
     * @return A [matcher environment][LixyDslMatchedMatcherEnvironment]
     * that uses the given (pseudo-)recognizers as its recognition technique.
     * @see toRecognizer
     */
    infix fun Any.isToken(token: LixyTokenType): LixyDslMatchedMatcherEnvironment {
        val recognizer = toRecognizer(this)
        val env = LixyDslMatchedMatcherEnvironment(recognizer, token)
        tokenMatchers += env
        return env
    }

    /**
     * Add an already defined matcher to this state
     */
    operator fun LixyTokenMatcher.unaryPlus() {
        tokenMatchers += this.selfBuildable()
    }

    /**
     * Anything that matches the given recognizer (or pseudo-recognizer) exactly
     * will be ignored when encountered. This would be equivalent to a `isToken`
     * that does not actually create any token.
     *
     * The matched sequence is skipped entirely by the lexer. No output is
     * emitted whatsoever.
     *
     * The given recognizer or pseudo-recognizer can be anything that
     * [toRecognizer] supports.
     *
     * @return A matcher environment that will produce a matcher that will make
     * the lexer ignore anything that it matches.
     */
    val Any.ignore: LixyDslIgnoringMatcherEnvironment
        get() {
            val env = LixyDslIgnoringMatcherEnvironment(toRecognizer(this))
            tokenMatchers += env
            return env
        }
}
