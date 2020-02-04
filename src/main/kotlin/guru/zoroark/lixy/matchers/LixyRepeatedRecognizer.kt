package guru.zoroark.lixy.matchers

class LixyRepeatedRecognizer(
    private val baseRecognizer: LixyTokenRecognizer,
    val min: Int = 1,
    val max: Int? = null
) :
    LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        var repetitions = 0
        var index = startAt
        var totalString = ""
        while (true) {
            val (ts, end) = baseRecognizer.recognize(s, index) ?: break
            repetitions += 1
            if (max != null && repetitions > max) {
                return null // Exceeded max allowed repetitions
            }
            index = end
            totalString += ts
        }
        return if (repetitions < min) null else totalString to index
    }
}

val String.repeated: LixyRepeatedRecognizer
    get() =
        LixyRepeatedRecognizer(LixyStringTokenRecognizer(this))

fun String.repeated(min: Int = 1, max: Int? = null): LixyRepeatedRecognizer =
    LixyRepeatedRecognizer(LixyStringTokenRecognizer(this), min, max)
