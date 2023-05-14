package me.xx2bab.extendagp.app

import me.xx2bab.extendagp.library1.HttpClient

class KtorClient : HttpClient {
    override fun post() {
        println("post() is called from KtorClient.")
    }
}