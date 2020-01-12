# [![Shinx](https://img.pokemondb.net/sprites/black-white/anim/normal/shinx.gif)](http://pokemondb.net/pokedex/shinx) Lixy, the lexer with a beautiful Kotlin DSL

WIP

```kotlin
enum class MyTokenTypes : LixyTokenTypes {
    DOT, WORD, WHITESPACE
}

val lexer = lixy {
    state {
        "." isToken DOT
        anyOf(" ", "\n", "\t") isToken WHITESPACE // TODO
        matchesRegex("[A-Za-z]+") isToken WORD // TODO
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
