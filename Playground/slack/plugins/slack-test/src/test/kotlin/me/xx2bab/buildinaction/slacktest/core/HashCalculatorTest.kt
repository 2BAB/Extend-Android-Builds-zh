package me.xx2bab.buildinaction.slacktest.core

import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class HashCalculatorTest {

    private val hashCalculator = HashCalculator()

    @Test
    fun md5_successful() {
        val res = hashCalculator.md5("abcdefghijklmn")
        assertThat(
            "MD5 calculation has some problem...",
            res == "0845a5972cd9ad4a46bad66f1253581f"
        )
    }

    @Test
    fun sha256_successful() {
        val res = hashCalculator.sha256("abcdefghijklmn")
        assertThat(
            "MD5 calculation has some problem...",
            res == "0653c7e992d7aad40cb2635738b870e4c154afb346340d02c797d490dd52d5f9"
        )
    }
}