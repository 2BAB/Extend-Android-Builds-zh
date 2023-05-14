package me.xx2bab.extendagp.app

import android.app.Activity
import android.os.Bundle
import me.xx2bab.extendagp.library1.LibraryApi

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Coming from the composite library
        val api = LibraryApi()
        // api.setClient(KtorClient())
        api.execute()
    }
}