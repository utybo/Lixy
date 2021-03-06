package guru.zoroark.lixy

/**
 * A token type. Can be pretty much anything.
 */
interface LixyTokenType

/**
 * A generic class for token types. [tokenType] returns tokens of this type.
 * Big lexers should be using an enum which implements [LixyTokenType] instead
 * of [tokenType] and [LixyGenericTokenType].
 *
 * @property name A name for this token type, useful for debugging.
 */
class LixyGenericTokenType(val name: String) : LixyTokenType {
    override fun toString(): String = "LixyGenericTokenType[$name]"
}

/**
 * Creates a new, distinct token type and returns it. The returned token type is
 * of the type [LixyGenericTokenType]
 *
 * @param name An optional name for the token type
 */
fun tokenType(name: String = ""): LixyGenericTokenType =
    LixyGenericTokenType(name)
