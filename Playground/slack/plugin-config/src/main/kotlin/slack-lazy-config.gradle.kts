import java.util.*

// Either plugins{} or apply<Plugin>() is ok,
// However import by plugins{} can support safe-accessor
// which makes IDE auto-complete work
plugins {
    id("me.2bab.buildinaction.provider-test")
    id("me.2bab.buildinaction.slack-lazy")

    // Tlack plugin will override the message property,
    // if you want to test the computeValue() execution,
    // please comment the tlack-lazy plugin below.
    id("me.2bab.buildinaction.tlack-lazy")
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
slackNotification1 {
    token.set(slackToken)
    channelId.set(slackChannelId)
//    message.set(computeValue())
//    message.set(provider { computeValue() })
//    println("slackNotification{} has been configured")
}

// If you try to pass computeValue for the property setting,
// it will not be triggered by the time `get()` calls, instead it's running immediately.
fun computeValue(): String {
    val sb = StringBuilder()
    (1..10).forEach { sb.append(it).append(",") }
    println("Message value has been computed")
    return sb.toString()
}