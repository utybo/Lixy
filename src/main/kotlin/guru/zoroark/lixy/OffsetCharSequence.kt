package guru.zoroark.lixy

internal class OffsetCharSequence(val charSequence: CharSequence, val offsetBy: Int) : CharSequence {
    init {
        if (offsetBy > charSequence.length)
            error("Invalid offset, exceeds original sequence's length")
    }
    override val length: Int
        get() = charSequence.length - offsetBy

    override fun get(index: Int): Char = charSequence[index + offsetBy]

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        charSequence.subSequence(startIndex + offsetBy, endIndex + offsetBy)

}

internal fun CharSequence.offsetBy(startAt: Int): CharSequence =
    OffsetCharSequence(this, startAt)