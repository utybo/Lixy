package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyDslStateEnvironment

/**
 * Create a recognizer that recognizes the given regular expression. Use
 * this before isToken to create a matcher that matches against a regular
 * expression.
 *
 * @param pattern The regular expression to use in the recognizer
 */
expect fun LixyDslStateEnvironment.matches(pattern: String): LixyTokenRecognizer