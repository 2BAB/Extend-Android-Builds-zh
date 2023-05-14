package me.xx2bab.buildinaction.slacktest.core

import me.xx2bab.buildinaction.slacktest.model.SlackMessageReqBody
import me.xx2bab.buildinaction.slacktest.model.SlackMessageRespBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// https://slack.com/api/ by default
interface SlackAPI {

    @POST("chat.postMessage")
    fun postMessage(
        @Header("Authorization") authorization: String,
        @Body message: SlackMessageReqBody
    ): Call<SlackMessageRespBody>


}