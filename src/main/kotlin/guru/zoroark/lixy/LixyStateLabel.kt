package guru.zoroark.lixy

/**
 * Represents a label for a state in a multiple labeled state context. Can be
 * implemented by anything.
 *
 * @see stateLabel
 */
interface LixyStateLabel

class LixyGenericStateLabel : LixyStateLabel

/**
 * Create a generic state label (actually an instance of [LixyGenericStateLabel]
 * and returns it. This function provides an easy and dirty way of creating
 * labels when you do not have an enum in place.
 */
fun stateLabel(): LixyGenericStateLabel = LixyGenericStateLabel()

/**
 * A special state label to be used in constructs like
 * [LixyTokenMatcher][guru.zoroark.lixy.matchers.LixyTokenMatcher]s
 */
object NoState : LixyStateLabel
