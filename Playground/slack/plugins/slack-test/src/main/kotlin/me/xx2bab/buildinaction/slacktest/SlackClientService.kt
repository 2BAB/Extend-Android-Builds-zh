package me.xx2bab.buildinaction.slacktest

import me.xx2bab.buildinaction.slacktest.core.Initializer
import me.xx2bab.buildinaction.slacktest.core.SlackClient
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters


abstract class SlackClientService : BuildService<BuildServiceParameters.None>, AutoCloseable {

    private val client: SlackClient = Initializer.getSlackClient()

    fun postNewMessage(
        token: String,
        channel: String,
        text: String
    ) = client.postNewMessage(token, channel, text)

    override fun close() {}
}