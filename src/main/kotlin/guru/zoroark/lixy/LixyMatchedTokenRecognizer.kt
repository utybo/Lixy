package guru.zoroark.lixy

internal class LixyMatchedTokenRecognizer(
    val recognizer: LixyTokenRecognizer,
    val tokenType: LixyTokenType
) : LixyTokenMatcher() {
    override fun match(s: String, startAt: Int): LixyToken? =
        recognizer.recognize(s, startAt)?.let { (recognizedSubstring, endsAt) ->
            LixyToken(recognizedSubstring, startAt, endsAt, tokenType)
        }

}
