# [![Shinx](https://img.pokemondb.net/sprites/black-white/anim/normal/shinx.gif)](http://pokemondb.net/pokedex/shinx) Lixy, the lexer with a beautiful Kotlin DSL

[![Actions Status](https://img.shields.io/github/workflow/status/utybo/Lixy/Tests?style=for-the-badge&logo=github&label=tests)](https://github.com/utybo/Lixy/actions)
[![Code Climate coverage](https://img.shields.io/codeclimate/coverage/utybo/Lixy?style=for-the-badge&logo=Code-Climate)](https://codeclimate.com/github/utybo/Lixy/test_coverage)
[![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability/utybo/Lixy?style=for-the-badge&logo=Code-Climate)](https://codeclimate.com/github/utybo/Lixy/maintainability)
![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-blue?logo=Kotlin&style=for-the-badge)
![Experimental](https://img.shields.io/badge/Stage-Experimental-red?style=for-the-badge)


[![Release](https://jitpack.io/v/guru.zoroark/lixy.svg?style=flat-square)](https://jitpack.io/#guru.zoroark/lixy) You can get the latest release/commit on JitPack.

## What is Lixy?

Lixy is a ["lexer"](https://en.wikipedia.org/wiki/Lexical_analysis) framework. It is a library that allows you to turn a string into a sequence of tokens using rules that you define using a Kotlin DSL.

This lexical analysis is typically the first step when making a compiler of any kind.

### A Kotlin DSL?

You will notice when looking at examples that Lixy uses a specific syntax that
might not look like real code at first. It is entirely valid Kotlin code! Lixy
uses a kind of "domain-specific language": a language within a language in this
case, specifically made to create lexers.

### Experimental!

Be careful, Lixy is still in an experimental stage! The API may (and will) break
constantly until a 0.1 version comes out. Lixy is already fairly usable, and
most functions and classes are already documented using KDoc, but there are no
user guides currently available. The best way to learn is by looking at the
tests (src/main/test/kotlin/guru/zoroark/lixy).

## Example

This simple example shows you what can be done using a single state.

```kotlin
// tokens.kt
enum class MyTokenTypes : LixyTokenType {
    DOT, WORD, WHITESPACE
}

// lexer.kt
import MyTokenTypes.*

val lexer = lixy {
    state {
        "." isToken DOT
        anyOf(" ", "\n", "\t") isToken WHITESPACE
        matches("[A-Za-z]+") isToken WORD
    }
}

val tokens = lexer.tokenize("Hello Kotlin.\n")
/* 
 * tokens = [
 *      ("Hello", 0, 5, WORD), 
 *      (" ", 5, 6, WHITESPACE), 
 *      ("Kotlin", 6, 11, WORD),
 *      (".", 11, 12, DOT),
 *      ("\n", 12, 13, WHITESPACE)
 *  ]
 */
```

This is fine, but we can do much more using multiple states, for example, a
string detector that differentiates string content from content that is not from
inside a string.

```kotlin
// tokens.kt
enum class TokenTypes : LixyTokenType {
    WORD, STRING_CONTENT, QUOTES, WHITESPACE
}

enum class Labels : LixyStateLabel {
    IN_STRING
}

// lexer.kt
val lexer = lixy {
    default state {
        " " isToken WHITESPACE
        matches("[a-zA-Z]+") isToken WORD
        "\"" isToken QUOTES thenState IN_STRING
    }
    IN_STRING state {
        // triple quotes to make it a raw string, so that we don't need to
        // escape everything
        matches("""(\\"|[^"])+""") isToken STRING_CONTENT
        "\"" isToken QUOTES thenState default
    }
}

val tokens = """Hello "Kotlin \"fans\"!" Hi"""
/* 
 * tokens = [
 *      (Hello, 0, 5, WORD), 
 *      ( , 5, 6, WHITESPACE), 
 *      (", 6, 7, QUOTES),
 *      (Kotlin \"fans\"!, 7, 23, STRING_CONTENT),
 *      (", 23, 24, QUOTES),
 *      ( , 24, 25, WHITESPACE),
 *      (Hi, 25, 27, WORD)
 *  ]
 */
```

There are a lot of possibilities!

[![Zoroark](https://img.pokemondb.net/sprites/black-white/anim/normal/zoroark.gif)](https://zoroark.guru)