package guru.zoroark.lixy

/**
 * A [LixyStringSetTokenRecognizer] is a [LixyTokenRecognizer] specifically
 * built to be able to recognize whether a substring of the input matches at a
 * relatively high speed. The speed improvements are mostly noticeable when the
 * input consists of strings of characters of the same length.
 *
 * Internally, this recognizer uses a map from integers (length of strings to
 * detect) to a set of strings (the strings of said length). This allows for a
 * fairly efficient recognizer.
 *
 * These recognizers should be created using the
 * [anyOf][StateDslEnvironment.anyOf] DSL function.
 */
class LixyStringSetTokenRecognizer(stringsToRecognize: List<String>) :
    LixyTokenRecognizer {
    private val criteria: Map<Int, Set<String>>

    init {
        val c = HashMap<Int, HashSet<String>>()
        stringsToRecognize.forEach {
            val x = c.getOrPut(it.length) { HashSet() }
            if (!x.add(it)) throw LixyException("Duplicate element in multi-string recognizer initialization")
        }
        criteria = c
    }

    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        for((len, possibilities) in criteria) {
            if(startAt + len > s.length)
                continue
            val sub = s.subSequence(startAt, startAt + len)
            if(possibilities.contains(sub))
                return sub.toString() to (startAt + len)
        }
        return null
    }

}