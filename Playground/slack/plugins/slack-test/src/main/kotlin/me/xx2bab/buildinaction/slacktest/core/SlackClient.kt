package me.xx2bab.buildinaction.slacktest.core

import me.xx2bab.buildinaction.slacktest.model.SlackMessageReqBody
import me.xx2bab.buildinaction.slacktest.model.SlackMessageRespBody

class SlackClient(private val slackAPI: SlackAPI) {

    fun postNewMessage(
        token: String,
        channel: String,
        text: String
    ): SlackMessageRespBody {
        val message = SlackMessageReqBody(channel = channel, text = text)
        return try {
            slackAPI.postMessage(authorization = "Bearer $token", message = message)
                .execute().body() ?: SlackMessageRespBody(false, "Empty body.")
        } catch (e: Exception) {
            SlackMessageRespBody(false, e.message ?: "Unknown exception.")
        }
    }

}