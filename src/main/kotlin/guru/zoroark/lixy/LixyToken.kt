package guru.zoroark.lixy

data class LixyToken(
    val string: String,
    val startsAt: Int,
    val endsAt: Int,
    val tokenType: LixyTokenType
)
