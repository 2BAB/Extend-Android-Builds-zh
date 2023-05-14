package me.xx2bab.buildinaction.slackcache

import java.net.URI

class DummyCloudClient {

    fun connect(username: String, password: String) {}

    fun disconnect() {}

    fun fetch(uri: URI): String {
        return "dummy result of $uri"
    }

}