package guru.zoroark.lixy.matchers

/**
 * A recognizer that, using another "base" recognizer, will recognize a
 * repetition of the other recognizer.
 *
 * @property baseRecognizer The recognizer which will be repeatedly used.
 * @property min The minimum amount of repetitions that has to be present for a
 * successful recognition.
 * @property max The maximum amount of repetitions that can be present for a
 * successful recognition.
 */
class LixyRepeatedRecognizer(
    val baseRecognizer: LixyTokenRecognizer,
    val min: Int = 1,
    val max: Int? = null
) :
    LixyTokenRecognizer {
    override fun recognize(s: String, startAt: Int): Pair<String, Int>? {
        var repetitions = 0
        var index = startAt
        var totalString = ""
        // Not a huge fan of the while true there, but idk how to make it less
        // trash-tier
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

/**
 * Create a recognizer that recognizes the given string 1 or more times in a
 * row.
 */
val String.repeated: LixyRepeatedRecognizer
    get() =
        LixyRepeatedRecognizer(LixyStringTokenRecognizer(this))

/**
 * Create a recognizer that recognizes the given string [min] to [max]
 * (inclusive) times in a row, by default from 1 and no maximum.
 *
 * @param min The minimum amount of repetitions required to get a successful
 * match. If the number of repetitions is below the minimum, the recognition
 * fails.
 *
 * @param max The maximum amount of repetitions required to get a successful
 * match, or null if no such limit should exist. If the number of repetitions
 * exceeds the maximum, the recognition fails.
 */
fun String.repeated(min: Int = 1, max: Int? = null): LixyRepeatedRecognizer =
    LixyRepeatedRecognizer(LixyStringTokenRecognizer(this), min, max)

/**
 * Create a recognizer that recognizes the given recognizer 1 or more times in a
 * row.
 */
val LixyTokenRecognizer.repeated: LixyRepeatedRecognizer
    get() = LixyRepeatedRecognizer(this)

/**
 * Create a recognizer that recognizes the given string [min] to [max]
 * (inclusive) times in a row, by default from 1 and no maximum.
 *
 * @param min The minimum amount of repetitions required to get a successful
 * match. If the number of repetitions is below the minimum, the recognition
 * fails.
 *
 * @param max The maximum amount of repetitions required to get a successful
 * match, or null if no such limit should exist. If the number of repetitions
 * exceeds the maximum, the recognition fails.
 */
fun LixyTokenRecognizer.repeated(min: Int = 1, max: Int? = null): LixyRepeatedRecognizer =
    LixyRepeatedRecognizer(this, min, max)