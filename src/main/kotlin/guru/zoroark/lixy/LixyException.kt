package guru.zoroark.lixy

/**
 * Generic parent class for exceptions thrown within the Lixy system
 */
open class LixyException(message: String, cause: Exception? = null) :
    Exception(message, cause)
