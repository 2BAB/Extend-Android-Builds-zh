package me.xx2bab.extendagp.library1

import okhttp3.OkHttp

class OkHttpClient: HttpClient {
    override fun post() {
        println("post() is called from OkHttpClient ${OkHttp.VERSION}.")
    }
}