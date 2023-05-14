package me.xx2bab.buildinaction.slacktest

import me.xx2bab.buildinaction.slacktest.base.EnvProperties
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class SlackClientServiceTest {

    @Test
    fun `SlackClientService posts successfully`() {
        val envProperties = EnvProperties()
        val slackToken = envProperties.slackToken
        val slackChannelId = envProperties.slackChannelId
        val msg = "built successfully"

        val project = ProjectBuilder.builder()
            .withName("slack-client-test")
            .build()

        val slackClientService = project.gradle.sharedServices
            .registerIfAbsent("SlackClientService", SlackClientService::class.java) {}
        val succResp = slackClientService.get()
            .postNewMessage(slackToken, slackChannelId, msg)
        assertThat("SlackClientService's post throws an error: ${succResp.error}",
            succResp.ok)
    }

    @Test
    fun `SlackClientService posts unsuccessfully`() {
        val envProperties = EnvProperties()
        val slackToken = envProperties.slackToken
        val slackChannelId = envProperties.slackChannelId
        val invalidSlackToken = "xxxxx-yyyyy-zzzzz"
        val invalidSlackChannelId = "123456789"
        val msg = "built successfully"

        val project = ProjectBuilder.builder()
            .withName("extension-test")
            .build()

        val slackClientService = project.gradle.sharedServices
            .registerIfAbsent("SlackClientService", SlackClientService::class.java) {}

        val invalidTokenResp = slackClientService.get()
            .postNewMessage(invalidSlackToken, slackChannelId, msg)
        assertThat("The token is supposed to be invalid but somehow the post worked...",
            invalidTokenResp.ok.not())

        val invalidChannelIdResp = slackClientService.get()
            .postNewMessage(slackToken, invalidSlackChannelId, msg)
        assertThat("The channel id is supposed to be invalid but somehow the post worked...",
            invalidChannelIdResp.ok.not())
    }

}