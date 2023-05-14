package me.xx2bab.buildinaction.slacktest.core

import me.xx2bab.buildinaction.slacktest.model.SlackConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Initializer {

    fun getSlackConfig(): SlackConfig = SlackConfig(token = "") // Update the `token` later

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(getSlackConfig().slackEndpoint)
        .addConverterFactory(GsonConverterFactory.create())
        .also {
//            val loggingInterceptor = HttpLoggingInterceptor()
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build()
//            it.client(okHttpClient)
        }
        .build()

    fun getSlackAPI(): SlackAPI = getRetrofit().create(SlackAPI::class.java)

    fun getSlackClient(): SlackClient = SlackClient(getSlackAPI())

}