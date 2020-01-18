package guru.zoroark.lixy

/**
 * A Buildable object is a "DSL environment" object that is used to construct
 * something of type [T]. The actual construction of the object is done with
 * the [build] function that must be implemented by subclasses.
 *
 * @param T The type of what this buildable object can construct.
 */
interface Buildable<out T> {
    fun build(): T
}
