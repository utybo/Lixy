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
        while (index < s.length) {
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
 * Create a recognizer that recognizes the given recognizer or pseudo-recognizer
 * 1 or more times in a row.
 *
 * This construct supports any (pseudo-)recognizer that is supported by
 * [toRecognizer].
 *
 * @return A [LixyRepeatedRecognizer] using the given
 * recognizer/pseudo-recognizer.
 */
val Any.repeated: LixyRepeatedRecognizer
    get() = LixyRepeatedRecognizer(toRecognizer(this))

/**
 * Create a recognizer that recognizes the given recognizer or pseudo-recognizer
 * [min] to [max] (inclusive) times in a row. By default, the
 * (pseudo)-recognizer is recognized from 1 to an infinite amount of times.
 *
 * Any recognizer or pseudo-recognizer that is supported by [toRecognizer] can
 * be used here.
 *
 * @param min The minimum amount of repetitions required to get a successful
 * match. If the number of repetitions is below the minimum, the recognition
 * fails.
 *
 * @param max The maximum amount of repetitions required to get a successful
 * match, or null if no such limit should exist. If the number of repetitions
 * exceeds the maximum, the recognition fails.
 *
 * @return A [LixyRepeatedRecognizer] that uses the constraints provided in
 * the parameters.
 */
fun Any.repeated(min: Int = 1, max: Int? = null): LixyRepeatedRecognizer =
    LixyRepeatedRecognizer(toRecognizer(this), min, max)