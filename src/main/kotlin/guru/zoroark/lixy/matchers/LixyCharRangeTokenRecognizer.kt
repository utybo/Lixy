package guru.zoroark.lixy.matchers

class LixyCharRangeTokenRecognizer(
    val detectedCharRange: ClosedRange<Char>
) : LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? =
        if(s[startAt] in detectedCharRange)
            s[startAt].toString() to startAt + 1
        else
            null
}
