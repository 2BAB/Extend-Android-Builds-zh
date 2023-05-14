package me.xx2bab.buildinaction.slacktest

import me.xx2bab.buildinaction.slacktest.base.EnvProperties
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.net.URI

class PrivateCloudServiceTest {

    @Test
    fun `The PrivateCloud Service can fetch the resource successfully`() {
        val project = ProjectBuilder.builder()
            .withName("private-cloud-service-test")
            .build()

        val privateCloudService = project.gradle.sharedServices
            .registerIfAbsent("PrivateCloudService", PrivateCloudService::class.java) {
                this.parameters.username.set("username")
                this.parameters.password.set("password")
            }
        val result = privateCloudService.get().fetch(URI("dummy-path"))
        assertThat(
            "DummyCloudClient fail to disconnect.",
            result == "dummy result of dummy-path"
        )
    }

}