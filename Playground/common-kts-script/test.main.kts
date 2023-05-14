@file:DependsOn("com.squareup.okhttp3:okhttp:3.8.1")

import okhttp3.*
import java.io.IOException

OkHttpClient().newCall(Request.Builder()
        .url("http://publicobject.com/helloworld.txt")
        .build())
        .enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }
            override fun onResponse(call: Call, resp: Response) {
                println(resp.body()!!.string())
            }
        })
