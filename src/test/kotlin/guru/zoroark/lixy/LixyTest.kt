package guru.zoroark.lixy

import kotlin.test.*

class LixyTest {
    @Test
    fun `Empty Lixy should crash`() {
        assertFailsWith<LixyException> {
            lixy {}
        }
    }
}