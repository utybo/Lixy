# [![Shinx](https://img.pokemondb.net/sprites/black-white/anim/normal/shinx.gif)](http://pokemondb.net/pokedex/shinx) Lixy Documentation

[README](README.md) | [CHANGELOG](CHANGELOG.md) | DOCUMENTATION

Welcome to the documentation page of Lixy!

Lixy in itself has some documentation in the form of KDoc strings (Kotlin's
Javadoc format). This page explains the basic principle behind Lixy.

## Using Lixy

This section will focus on the use of Lixy as a lexer library, specifically how
to operate its DSL.

Have a quick look at the
[Wikipedia page for lexers](https://en.wikipedia.org/wiki/Lexical_analysis) to
understand what lexers are. They are a fairly simple concept.

* Take a string of characters
* Turn it into a sequence of tokens

Lexing is the first step when making compilation software, but can also be used
in a variety of other contexts.

So, how can you use Lixy?

### The Lixy flow

Lixy works in the following flow:

* Take a string at a specific starting point
* Check if the beginning of said string matches some pattern(s)
  * If no, failure
  * If yes, associate this match with a token type: this gives you a token.
* Repeat taking the string at the end of the previous token until the end of the
  string is reached

Usually, the matching process is separated in two steps:

* Attempt to recognize the string: does it match some pattern? A recognizer is 
  able to tell is a match is successful and what was matched, but has no 
  information on what its associated token type is
* Match the result of the recognizer with something, usually with a token type.

That is a fairly oversimplified view of the process of making a lexer in Lixy however.

### My first lexer

```kotlin
lixy {
    state {
        // ...
    }
}
```

This is the usual construct for single-state lexers. We will see what
single-state means later on.

This is entirely Kotlin code, which you can use wherever you like. This creates
a (reusable) lexer, which is returned by the `lixy` function, so you must store
the returned value somewhere.

```kotlin
val myLexer = lixy {
    state {
        // ...
    }
}
```

However, here, our lexer does not do anything. This is because we have not given
it any rules on how to match what. It will immediately crash when you try to use
it.

Let's thus add some rules, for which we'll need token types.

There are two ways of creating token types: creating them on the fly, or storing
them in an enum. In the following examples, we will create them on the fly, but
in real applications, you should consider using an enum.

```kotlin
val ta = tokenType()    // 1
val tb = tokenType()    // 1
val myLexer = lixy {    // 2
    state {             // 3
        "a" isToken ta  // 4
        "b" isToken tb  // 5
    }
}
```

1. The `tokenType()` function, which can be called from anywhere, simply creates
   a token type.
2. You will *always* use `lixy {}` when creating a lexer with Lixy's DSL.
3. `state {}` is the area in which you can declare your rules.
4. This is the first rule, one that is fairly simple: if you encounter the
   string `a`, then emit a token of token type `ta`
5. This is similar to 4, but the rule is slightly different: if we encounter the
   string `b`, we then emit a token of token type `tb`

We can then use this lexer:

```kotlin
val tokensList = myLexer.tokenize("abab")
/* 
 * tokensList = [
 *   LixyToken("a", 0, 1, ta),
 *   LixyToken("b", 1, 2, tb),
 *   LixyToken("a", 2, 3, ta),
 *   LixyToken("b", 3, 4, tb)
 * ]
 */
```

The resulting list is a list of tokens, which contain (in order):

* The token's string (what was matched)
* Its position, with first the starting index (inclusive) and the ending index
  (exclusive). For example, in "abcde" the substring "cd" would have a starting
  index of 2 and an ending index of 4. Note that starting index + length =
  ending index.
* The token type, which is the same object as what was described in `isToken`

Of course, this example also works for longer strings:

```kotlin
val tbanana = tokenType()       
val tapple = tokenType()
val tspace = tokenType()
val fruitLexer = lixy {            
    state {                     
        "banana" isToken tbanana
        "apple" isToken tapple
        " " isToken tspace
    }
}
val tokensList = fruitLexer.tokenize("apple banana banana")
/* 
 * tokensList = [
 *   LixyToken("apple", 0, 5, tapple),
 *   LixyToken(" ", 5, 6, tspace),
 *   LixyToken("banana", 6, 12, tbanana),
 *   LixyToken(" ", 12, 13, tspace),
 *   LixyToken("banana", 13, 19, tbanana)
 * ]
 */

```

### Order matters

Matchers are always checked in the order they are declared within a `state {}`
in Lixy. This means that you will end up with unexpected results if you do not
pay attention.

```kotlin
val tlong = tokenType()
val tshort = tokenType()
val lexerAbcFirst = lixy {  // 1
    state {
        "abc" isToken tlong
        "a" isToken tshort
    }
}
val tokensAbcFirst = lexerAbcFirst.tokenize("aabca")
/*
 * tokensAbcFirst = [
     LixyToken("a", 0, 1, tshort),
     LixyToken("abc", 1, 4, tlong),
     LixyToken("a", 4, 5, tshort)
 * ]
 */
val lexerAFirst = lixy {    // 2
    state {
        "a" isToken tshort
        "abc" isToken tlong
    }
}
val tokensAFirst = lexerAFirst.tokenize("aabca")
/*
 * tokensAFirst = [
 *   LixyToken("a", 0, 1, tshort),
 *   LixyToken("a", 1, 2, tshort),
 *   ERROR: at index 2, nothing matches "b", "bc" or "bca"
 * ]
 */
```

In this example, the first one (1) works just fine. At every point, it will
first check against "abc", then against "a".

With our original string being "aabca":

* Start at index 0 (seen "aabca")
  * Try to match with "abc" from the beginning: does not work
  * Try to match with "a": matches and ends at index 1
* Continue at index 1 (seen "abca")
  * Try to match with "abc": matches and ends at index 4
* Continue at index 4 (seen "a")
  * Try to match with "abc": does not work
  * Try to match with "a": matches and ends at index 5
* Success

If we take the second one however, we end up with this situation:

* Start at index 0 (seen "aabca")
  * Try to match with "a": matches and ends at index 1
* Continue at index 1 (seen "abca")
  * Try to match with "a": matches and ends at index 2
* Continue at index 2 (seen "bca")
  * Try to match with "a": does not work
  * Try to match with "abc": does not work
* ERROR: Did not match anything.

### More interesting recognizers

TODO
