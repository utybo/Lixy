package guru.zoroark.lixy

interface LixyTokenMatcher {
    fun match(s: String, startAt: Int): LixyToken?
}
