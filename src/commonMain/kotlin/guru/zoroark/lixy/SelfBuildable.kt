package guru.zoroark.lixy

class SelfBuildable<T>(private val what: T) : Buildable<T> {
    override fun build(): T = what
}

fun <T> T.selfBuildable(): Buildable<T> = SelfBuildable(this)