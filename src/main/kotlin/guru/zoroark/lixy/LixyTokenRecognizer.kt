package guru.zoroark.lixy

interface LixyTokenRecognizer {
    fun recognize(s: String, startAt: Int): Pair<String, Int>?
}
