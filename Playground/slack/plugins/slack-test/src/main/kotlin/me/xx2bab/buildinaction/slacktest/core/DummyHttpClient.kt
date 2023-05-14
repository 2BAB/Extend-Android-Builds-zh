package me.xx2bab.buildinaction.slacktest.core

import java.io.IOException
import java.net.URI
import java.util.concurrent.atomic.AtomicBoolean

class DummyCloudClient {

    val connectionState = AtomicBoolean(false)

    fun connect(username: String, password: String) {
        connectionState.set(true)
    }

    fun disconnect() {
        connectionState.set(false)
    }

    @Throws(UnsupportedOperationException::class, IOException::class)
    fun fetch(uri: URI): String {
        if (connectionState.get().not()) {
            throw UnsupportedOperationException("Please connect to the server before calling fetch().")
        }
        if (uri.toString().contains("private")) {
            throw IOException("Access denied.")
        }
        return "dummy result of $uri"
    }

}