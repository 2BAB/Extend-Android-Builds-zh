package me.xx2bab.buildinaction.slackorchestra

import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.net.URI


abstract class PrivateCloudService
    : BuildService<PrivateCloudService.Params>, AutoCloseable {

    internal interface Params : BuildServiceParameters {
        val username: Property<String>
        val password: Property<String>
    }

    private val client: DummyCloudClient

    init {
        val username = parameters.username.get()
        val password = parameters.password.get()
        client = DummyCloudClient()
        client.connect(username, password)
    }

    fun fetch(uri: URI) = client.fetch(uri)

    override fun close() = client.disconnect()
}