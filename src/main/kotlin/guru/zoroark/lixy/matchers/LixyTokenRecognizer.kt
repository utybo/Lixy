package guru.zoroark.lixy.matchers

interface LixyTokenRecognizer {
    fun recognize(s: String, startAt: Int): Pair<String, Int>?
}
