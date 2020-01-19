package guru.zoroark.lixy

interface LixyStateLabel

class LixyGenericStateLabel : LixyStateLabel

fun stateLabel(): LixyGenericStateLabel = LixyGenericStateLabel()
