package guru.zoroark.lixy

import java.util.regex.Pattern

/**
 * This classed is used to build a lexer state ([LixyState]) using a DSL, and
 * is used inside a lambda-with received. It also defines high level functions
 * for constructing matchers and using custom matchers.
 *
 * Matchers are constructed immediately, but states themselves are only
 * constructed when calling [build].
 */
class StateDslEnvironment : Buildable<LixyState> {
    private val tokenMatchers = mutableListOf<LixyTokenMatcher>()

    override fun build(): LixyState {
        return LixyState(mutableListOf<LixyTokenMatcher>().apply {
            addAll(tokenMatchers)
        })
    }

    /**
     * Add a matcher that attempts to match against the given string exactly,
     * returning a token with the given token type in case of a match
     */
    infix fun String.isToken(token: LixyTokenType) {
        tokenMatchers += LixyStringTokenMatcher(this, token)
    }

    /**
     * Add an already defined matcher to this state
     */
    operator fun LixyTokenMatcher.unaryPlus() {
        tokenMatchers += this
    }

    fun matches(regex: String): LixyTokenRecognizer =
        RegexPatternRecognizer(Pattern.compile(regex))

    infix fun LixyTokenRecognizer.isToken(tokenType: LixyTokenType) {
        tokenMatchers += LixyMatchedTokenRecognizer(this, tokenType)
    }
}
