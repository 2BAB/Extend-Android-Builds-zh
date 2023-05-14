package me.xx2bab.buildinaction.slacktest.core

import java.security.MessageDigest

class HashCalculator {

    fun md5(input: String): String {
        return hashString(input, "MD5")
    }

    fun sha256(input: String): String {
        return hashString(input, "SHA-256")
    }

    private fun hashString(input: String, algorithm: String): String {
        return MessageDigest.getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

}