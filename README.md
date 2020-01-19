# [![Shinx](https://img.pokemondb.net/sprites/black-white/anim/normal/shinx.gif)](http://pokemondb.net/pokedex/shinx) Lixy, the lexer with a beautiful Kotlin DSL

[![Actions Status](https://img.shields.io/github/workflow/status/utybo/Lixy/Tests?style=for-the-badge&logo=github&label=tests)](https://github.com/utybo/Lixy/actions) [![Code Climate coverage](https://img.shields.io/codeclimate/coverage/utybo/Lixy?style=for-the-badge&logo=Code-Climate)](https://codeclimate.com/github/utybo/Lixy/test_coverage) [![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability/utybo/Lixy?style=for-the-badge&logo=Code-Climate)](https://codeclimate.com/github/utybo/Lixy/maintainability) ![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-blue?logo=Kotlin&style=for-the-badge) ![Experimental](https://img.shields.io/badge/Stage-Experimental-red?style=for-the-badge)

WIP

```kotlin
// tokens.kt
enum class MyTokenTypes : LixyTokenTypes {
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

[![Zoroark](https://img.pokemondb.net/sprites/black-white/anim/normal/zoroark.gif)](https://zoroark.guru)