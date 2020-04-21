package guru.zoroark.lixy

/**
 * A [LixyToken] is a data class representing a token found through the
 * tokenization process of a [LixyLexer] and the matching process of a
 * [LixyTokenMatcher][guru.zoroark.lixy.matchers.LixyTokenMatcher]. It has
 * information on where the token begins, where it ends, its type, and what it
 * actually represents.
 */
data class LixyToken(
    /**
     * The string this token represents. This is exactly what was match and, as
     * such, is a substring of the original lexed string
     */
    val string: String,
    /**
     * The index where [string] starts in the original string.
     *
     * For example: Token "world" in "Hello world" has startsAt equal to 6
     */
    val startsAt: Int,
    /**
     * The index after [string] ends in the original string (i.e. exclusive).
     *
     * For example: Token "Hello" in "Hello world" has endsAt equal to 5
     */
    val endsAt: Int,
    /**
     * The token type this token corresponds to
     */
    val tokenType: LixyTokenType
)
