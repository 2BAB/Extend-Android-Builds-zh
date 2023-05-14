package me.xx2bab.buildinaction.slacktest

import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class SlackNotificationExtensionTest {

    @Test
    fun `extension integrity test`() {
        val slackToken = "xxxxx-yyyyy-zzzzz"
        val slackChannelId = "123456789"
        val msg = "built successfully"
        val pkgId = "id"

        val project = ProjectBuilder.builder()
            .withName("extension-test")
            .build()

        val slackExt = project.extensions.create(
            "slackNotification",
            SlackNotificationExtension::class.java
        ).apply {
            enabled.set(true)
            defaultConfig {
                message.set(msg)
                pkg {
                    id.set(pkgId)
                }
            }
            channels.apply {
                register("androidTeam") {
                    token.set(slackToken)
                    channelId.set(slackChannelId)
                }
                register("androidTeam2") {
                    token.set(slackToken)
                    channelId.set(slackChannelId)
                }
            }

            selectChannelsByVariant { variant, channel ->
                !(variant == "debug" && channel == "mobileTeam")
            }
        }

        assertThat("The basic Property<Boolean> usage (enabled) does not work well.",
            slackExt.enabled.get())
        assertThat("The nested Property usage (defaultConfig.message) does not work well.",
            slackExt.defaultConfig.message.get() == msg)
        assertThat("The nested Property usage (defaultConfig.pkg.id) does not work well.",
            slackExt.defaultConfig.pkg.id.get() == pkgId)
        assertThat("The NamedDomainObjectContainer (channels) size is wrong.",
            slackExt.channels.toList().size == 2)
    }
}