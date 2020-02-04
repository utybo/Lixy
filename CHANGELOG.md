# [![Shinx](https://img.pokemondb.net/sprites/black-white/anim/normal/shinx.gif)](http://pokemondb.net/pokedex/shinx) Lixy Changelog

[README](README.md) | CHANGELOG | [DOCUMENTATION](DOCUMENTATION.md)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com), and this project adheres to [Semantic Versioning](https://semver.org).

Breaking DSL changes are marked with **BRK DSL**. Breaking API changes that do not impact the DSL are marked with **BRK API**.


## [Unreleased]
**Initial release.** Unstable, experimental.

### Added
* Recognizer and Matcher system
* RegExp, exact string, any-of-string-set matchers/recognizers added
* Matchers can skip sequences instead of emitting tokens
* `Buildable` interface for declaring things that can be built through the DSL
* DSL for the lexer, states and the individual matchers
* Lexer system with single state or multiple states
* Tests for the lexer DSL
* CHANGELOG and README files
* JitPack CD, GitHub Actions CI, Gradle files, Code Climate configuration