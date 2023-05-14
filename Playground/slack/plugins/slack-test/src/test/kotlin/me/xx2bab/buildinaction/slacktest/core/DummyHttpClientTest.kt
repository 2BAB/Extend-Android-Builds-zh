package me.xx2bab.buildinaction.slacktest.core

import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.net.URI

/**
 * A client with network requesting usually did not run such unit testing,
 * should mock the networking part and compose it into the client constructor.
 * Here is just a dummy client to demonstrate the basic procedure.
 */
class DummyHttpClientTest {

    @Test
    fun `The client connects successfully`() {
        val client = DummyCloudClient()
        client.connect("2BAB", "dummy-pass")
        assertThat("DummyCloudClient fail to connect.", client.connectionState.get())
    }

    @Test
    fun `The client disconnects without live-connection but still ok`() {
        val client = DummyCloudClient()
        client.disconnect()
        assertThat(
            "DummyCloudClient fail to disconnect (without connection).",
            client.connectionState.get().not()
        )
    }

    @Test
    fun `The client connected normally`() {
        val client = DummyCloudClient()
        client.connect("2BAB", "dummy-pass")
        client.disconnect()
        assertThat("DummyCloudClient fail to disconnect.", client.connectionState.get().not())
    }

    @Test
    fun `Fetch resources successfully`() {
        val client = DummyCloudClient()
        client.connect("2BAB", "dummy-pass")
        val result = client.fetch(URI("dummy-path"))
        assertThat(
            "DummyCloudClient fail to disconnect.",
            result == "dummy result of dummy-path"
        )
    }

    @Test
    fun `Throw UnsupportedException when fetch resources`() {
        val client = DummyCloudClient()
        assertThrows<UnsupportedOperationException>(
            "When client is not connected, should throw UnsupportedOperationException " +
                    "if the user call the `fetch(...)`."
        ) {
            client.fetch(URI("dummy-path"))
        }
    }

    @Test
    fun `Throw IOException when fetch resources`() {
        val client = DummyCloudClient()
        client.connect("2BAB", "dummy-pass")
        assertThrows<IOException>(
            "When fetch() is retrieve some res from remote server," +
                    " it may throws IOException."
        ) {
            client.fetch(URI("ftp://abc.com/private/node"))
        }
    }

}