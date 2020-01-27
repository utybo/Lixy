package guru.zoroark.lixy.samples

import guru.zoroark.lixy.LixyStateLabel
import guru.zoroark.lixy.LixyTokenType

enum class StringDetectorTypes: LixyTokenType {
    word, whitespace, quotes, stringContent, punctuation
}

enum class StringDetectorLabels: LixyStateLabel {
    inString
}