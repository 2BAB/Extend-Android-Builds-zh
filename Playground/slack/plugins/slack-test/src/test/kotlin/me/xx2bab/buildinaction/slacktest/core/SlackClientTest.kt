package me.xx2bab.buildinaction.slacktest.core

import io.mockk.every
import io.mockk.mockk
import me.xx2bab.buildinaction.slacktest.model.SlackMessageRespBody
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

// https://api.slack.com/methods/chat.postMessage/test
class SlackClientTest {

    val token = ""
    val channel = ""
    val text = ""

    @Test
    fun postNewMessage_successful() {
        val mockedSlackAPI = createMockedSlackAPI(SlackMessageRespBody(true, null))
        val slackClient = SlackClient(mockedSlackAPI)
        val result = slackClient.postNewMessage(token, channel, text)
        assertThat("A normal posting flow has unexpected error. ${result.error}", result.ok)
    }

    @Test
    fun postNewMessage_unsuccessful_invalidToken() {
        val mockedSlackAPI = createMockedSlackAPI(SlackMessageRespBody(false, "invalid_auth"))
        val slackClient = SlackClient(mockedSlackAPI)
        val result = slackClient.postNewMessage(token, channel, text)
        assertThat("Token should be invalid but somehow the post is working.", result.ok.not())
        println(result.error)
        assertThat(
            "Token should be invalid but the error msg changed.",
            (result.ok.not() && result.error == "invalid_auth")
        )
    }

    @Test
    fun postNewMessage_unsuccessful_emptyBody() {
        val mockedSlackAPI = createMockedSlackAPI(null)
        val slackClient = SlackClient(mockedSlackAPI)
        val result = slackClient.postNewMessage(token, channel, text)
        assertThat("Body should be empty but somehow the post is working.", result.ok.not())
        assertThat(
            "Body should be empty but the error msg changed.",
            (result.ok.not() && result.error == "Empty body.")
        )
    }

    @Test
    fun postNewMessage_unsuccessful_ioException() {
        val mockedSlackAPI = createMockedSlackAPI(null, IOException("Timeout"))
        val slackClient = SlackClient(mockedSlackAPI)
        val result = slackClient.postNewMessage(token, channel, text)
        assertThat("Post should be timeout but somehow it is working.", result.ok.not())
        assertThat(
            "Post should be timeout with the message 'Timeout' but somehow it is working.",
            (result.ok.not() && result.error == "Timeout")
        )
    }

    private fun createMockedSlackAPI(resp: SlackMessageRespBody?, exception: Exception? = null): SlackAPI {
        val mockedSlackAPI = mockk<SlackAPI>()
        val mockedCall = mockk<Call<SlackMessageRespBody>>()
        val mockedResp = mockk<Response<SlackMessageRespBody>>()
        every { mockedSlackAPI.postMessage(any(), any()) } returns mockedCall
        if (exception != null) {
            every { mockedCall.execute() } throws exception
        } else {
            every { mockedCall.execute() } returns mockedResp
            every { mockedResp.body() } returns resp
        }
        return mockedSlackAPI
    }
}
