package guru.zoroark.lixy

internal const val LIXY_WEBSITE = "lixy.zoroark.guru"

fun lixy(body: LixyDslEnvironment.() -> Unit): LixyLexer {
    val dslEnv = LixyDslEnvironment()
    body(dslEnv)
    return dslEnv.build().also {
        if (it.states.isEmpty())
            throw LixyException("Empty body is not allowed. Need help? Visit $LIXY_WEBSITE")
    }

}