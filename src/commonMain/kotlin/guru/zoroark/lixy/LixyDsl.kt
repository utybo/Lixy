package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.*

/**
 * URL to the website of Lixy. Please change this if you fork this project!
 */
internal const val LIXY_WEBSITE = "lixy.zoroark.guru"

/**
 * Creates a [LixyLexer] object using the Lixy DSL, where [body] receives
 * a [LixyDslEnvironment] that can be used to modify the lexer that is
 * eventually returned.
 */
fun lixy(body: LixyDslEnvironment.() -> Unit): LixyLexer {
    val dslEnv = LixyDslEnvironment()
    body(dslEnv)
    return dslEnv.build().also {
        if (it.statesCount == 0)
            throw LixyException("Empty body is not allowed. Need help? Visit $LIXY_WEBSITE")
    }

}

/**
 * Simple function to create a matcher. The returned [LixyTokenMatcher]
 * executes the given lambda and returns its value and nothing else.
 *
 * The behavior of a matcher is described in [LixyTokenMatcher]
 */
fun matcher(
    nextState: LixyNextStateBehavior = LixyNoStateChange,
    matcherBody: (s: String, startAt: Int) -> LixyToken?
): LixyTokenMatcher =
    object : LixyTokenMatcher() {
        override fun match(s: String, startAt: Int): LixyMatcherResult =
            matcherBody(s, startAt)?.let {
                LixyMatchedTokenResult(it, nextState)
            } ?: LixyNoMatchResult
    }