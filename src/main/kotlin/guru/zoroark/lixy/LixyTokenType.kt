package guru.zoroark.lixy

/**
 * A token type. Can be pretty much anything.
 */
interface LixyTokenType

/**
 * A generic class for token types. [tokenType] returns tokens of this type.
 * Big lexers should be using an enum which implements [LixyTokenType] instead
 * of [tokenType] and [LixyGenericTokenType].
 */
class LixyGenericTokenType :
    LixyTokenType

/**
 * Creates a new, distinct token type and returns it. The returned token type is
 * of the type [LixyGenericTokenType]
 */
fun tokenType(): LixyGenericTokenType =
    LixyGenericTokenType()
