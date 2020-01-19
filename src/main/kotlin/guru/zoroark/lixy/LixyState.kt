package guru.zoroark.lixy

import guru.zoroark.lixy.matchers.LixyTokenMatcher

/**
 * A state that contains matchers. The list of matchers is tested sequentially
 * in order.
 */
data class LixyState(val matchers: List<LixyTokenMatcher>)
