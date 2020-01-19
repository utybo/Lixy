package guru.zoroark.lixy

/**
 * Implementation of a [LixyTokenMatcher] that attempts to match against a
 * given string exactly. It will return a token with the given token type if
 * such a match is found.
 */
class LixyStringTokenMatcher(val match: String, val tokenType: LixyTokenType) :
    LixyTokenMatcher() {
    override fun match(s: String, startAt: Int): LixyToken? {
        if (startAt + match.length > s.length) {
            return null // Cannot match (match goes beyond s boundaries)
        }
        for (i in match.indices) {
            if (s[i + startAt] != match[i])
                return null // Does not match
        }
        // Everything matched, return a token
        return LixyToken(
            string = s.substring(startAt, startAt + match.length),
            startsAt = startAt,
            endsAt = startAt + match.length,
            tokenType = tokenType
        )
    }

}
