package me.xx2bab.buildinaction.slacktest.base

object SlackConfigs {

    fun regular(token: String, channel: String) = """
        slackNotification5 {
            enabled.set(true)
            defaultConfig {
                message.set("Slack Notification project built successfully with advanced DSL!")
                pkg {
                    id.set("id")
                }
            }
            channels {
                register("androidTeam") {
                    token.set("$token")
                    channelId.set("$channel")
                }
                register("androidTeam2") {
                    token.set("$token")
                    channelId.set("$channel")
                }
            }
        
            selectChannelsByVariant { variant, channel ->
                !(variant == "debug" && channel == "mobileTeam")
            }
        }
    """

    val partialConfiguratedOnly = """
        slackNotification5 {
            enabled.set(true)
        }
    """.trimIndent()
}