package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.LixyTokenMatcher
import guru.zoroark.lixy.matchers.LixyTokenRecognizer
import guru.zoroark.lixy.matchers.LixyTokenRecognizerIgnored

/**
 * A DSL environment for constructing ignoring matchers. This builds into a
 * [LixyTokenRecognizerIgnored].
 */
class LixyDslIgnoringMatcherEnvironment(
    /**
     * The recognizer that will be used by the matcher.
     */
    baseRecognizer: LixyTokenRecognizer
): LixyDslGenericMatcherEnvironment(baseRecognizer) {
    override fun build(): LixyTokenMatcher =
        LixyTokenRecognizerIgnored(
            baseRecognizer,
            nextStateBehavior
        )
}