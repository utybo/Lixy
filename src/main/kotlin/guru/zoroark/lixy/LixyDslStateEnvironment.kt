package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*
import guru.zoroark.lixy.matchers.RegexPatternRecognizer
import org.intellij.lang.annotations.Language
import java.util.regex.Pattern

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
     * Add a matcher that attempts to match against the given string exactly,
     * returning a token with the given token type in case of a match
     */
    infix fun String.isToken(token: LixyTokenType): LixyDslMatcherEnvironment =
        LixyDslMatcherEnvironment(LixyStringTokenRecognizer(this), token).also {
            tokenMatchers += it
        }

    /**
     * Add an already defined matcher to this state
     */
    operator fun LixyTokenMatcher.unaryPlus() {
        tokenMatchers += this.selfBuildable()
    }

    /**
     * Create a recognizer that recognizes the given regular expression. Use
     * this before [isToken] to create a matcher that matches against a regular
     * expression.
     *
     * @param regex The regular expression to use in the recognizer
     * @see isToken
     */
    fun matches(@Language("RegExp") regex: String): LixyTokenRecognizer =
        RegexPatternRecognizer(
            Pattern.compile(regex)
        )

    /**
     * Add a matcher that attempts to match against the given recognizer,
     * returning a token with the given token type in case of a successfully
     * recognized substring.. The exact pattern that is recognized is entirely
     * up to the recognizer.
     */
    infix fun LixyTokenRecognizer.isToken(tokenType: LixyTokenType): LixyDslMatcherEnvironment =
        LixyDslMatcherEnvironment(
            this,
            tokenType
        ).also {
            tokenMatchers += it
        }

    /**
     * Crate a recognizer that recognizes any of the strings provided as
     * parameters.
     *
     * @param s Strings that should be recognized
     * @return A string recognizer. Use [isToken] to make it a usable matcher.
     */
    fun anyOf(vararg s: String): LixyTokenRecognizer =
        if (s.isEmpty())
            throw LixyException("anyOf() must have at least one string argument")
        else
            LixyStringSetTokenRecognizer(
                s.asList()
            )
}
