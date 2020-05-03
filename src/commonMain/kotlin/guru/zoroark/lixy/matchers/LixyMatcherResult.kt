package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyToken

/**
 * Subclasses of this class represent the different possible outputs for
 * matchers.
 * @see LixyNoMatchResult
 * @see LixyIgnoreMatchResult
 * @see LixyMatchedTokenResult
 */
sealed class LixyMatcherResult

/**
 * Indicates that the match was not successful, that there was no match.
 */
object LixyNoMatchResult : LixyMatcherResult()

/**
 * Indicates that the match was successful, but no token should be created for
 * this match.
 *
 * @property tokenEndsAt Where the ignored token ends (exclusive, this is the
 * index where the lexer will resume)
 * @property nextStateBehavior The behavior to follow after processing this
 * match regarding states
 */
class LixyIgnoreMatchResult(val tokenEndsAt: Int, val nextStateBehavior: LixyNextStateBehavior) : LixyMatcherResult()

/**
 * Indicates that the match was successful and a token was created.
 *
 * @property token The token that is the result of the match.
 */
class LixyMatchedTokenResult(val token: LixyToken, val nextStateBehavior: LixyNextStateBehavior) : LixyMatcherResult()