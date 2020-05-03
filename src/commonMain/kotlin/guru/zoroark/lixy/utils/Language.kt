package guru.zoroark.lixy.utils

import kotlin.annotation.AnnotationTarget.*


/**
 * Mapping to `org.intellij.lang.annotations.Language` on JVM, but no actual
 * implementations on other platforms.
 *
 * Credits to @Him188 on GitHub, thanks!
 */
@Retention(AnnotationRetention.BINARY)
@Target(FUNCTION, FIELD, VALUE_PARAMETER, LOCAL_VARIABLE, ANNOTATION_CLASS)
@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
expect annotation class Language(
    val value: String,
    val prefix: String, // default values aren't allowed yet
    val suffix: String
)