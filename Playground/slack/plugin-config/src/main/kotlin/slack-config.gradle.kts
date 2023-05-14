import java.util.*

// Either plugins{} or apply<Plugin>() is ok,
// However import by plugins{} can support safe-accessor
// which makes IDE auto-complete work
plugins {
    id("me.2bab.buildinaction.slack")
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
slackNotification {
    token = slackToken
    channelId = slackChannelId
    message = "Slack Notification project built successfully!"
}