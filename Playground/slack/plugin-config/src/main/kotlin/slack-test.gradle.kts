import java.util.*

// Either plugins{} or apply<Plugin>() is ok,
// However import by plugins{} can support safe-accessor
// which makes IDE auto-complete work
plugins {
    id("me.2bab.buildinaction.slack-test")
}

val propFile = File(project.rootProject.rootDir, "local.properties")
val localProperties = Properties()
if (propFile.exists()) {
    localProperties.load(propFile.inputStream())
}
val slackToken = if (propFile.exists()) localProperties["slack.token"].toString() else ""
val slackChannelId = if (propFile.exists()) localProperties["slack.channelId"].toString() else ""

// If you used apply<Plugin>() before, should use configure<SlackNotificationExtension>{} here
// to config SlackNotificationExtension
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
// You can use it by property access because we export defaultConfig as public modifier.
slackNotification5.defaultConfig.message.set("Slack Notification project built successfully with advanced DSL!!")

// If configure the slackNotification{} in a Gradle Groovy DSL,
// you can use the assignment (=) operator as a convenience.
//slackNotification {
//    enabled = true
//    token = slackToken
//    channelId = slackChannelId
//    message = "Slack Notification project built successfully with advanced DSL!"
//}

