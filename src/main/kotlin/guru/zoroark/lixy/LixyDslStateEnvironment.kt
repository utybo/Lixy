package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*
import guru.zoroark.lixy.matchers.LixyMatchedTokenRecognizer
import guru.zoroark.lixy.matchers.RegexPatternRecognizer
import java.util.regex.Pattern

/**
 * This classed is used to build a lexer state ([LixyState]) using a DSL, and
 * is used inside a lambda-with received. It also defines high level functions
 * for constructing matchers and using custom matchers.
 *
 * Matchers are constructed immediately, but states themselves are only
 * constructed when calling [build].
 */
class LixyDslStateEnvironment : Buildable<LixyState> {
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
        tokenMatchers += LixyStringTokenMatcher(
            this,
            token
        )
    }

    /**
     * Add an already defined matcher to this state
     */
    operator fun LixyTokenMatcher.unaryPlus() {
        tokenMatchers += this
    }

    fun matches(regex: String): LixyTokenRecognizer =
        RegexPatternRecognizer(
            Pattern.compile(regex)
        )

    /**
     * Add a matcher that attempts to match against the given recognizer,
     * returning a token with the given token type in case of a successfully
     * recognized substring.. The exact pattern that is recognized is entirely
     * up to the recognizer.
     */
    infix fun LixyTokenRecognizer.isToken(tokenType: LixyTokenType) {
        tokenMatchers += LixyMatchedTokenRecognizer(
            this,
            tokenType
        )
    }

    /**
     * Crate a recognizer that recognizes any of the strings provided as
     * parameters.
     *
     * @param s Strings that should be recognized
     * @return A string recognizer. Use [isToken] to make it a usable matcher.
     */
    fun anyOf(vararg s: String): LixyTokenRecognizer =
        if(s.isEmpty())
            throw LixyException("anyOf() must have at least one string argument")
        else
            LixyStringSetTokenRecognizer(
                s.asList()
            )
}
