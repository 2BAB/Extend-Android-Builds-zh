package me.xx2bab.buildinaction.slacktest.base

import java.io.File
import java.util.*

class EnvProperties {

    val slackToken: String
    val slackChannelId: String

    init {
        val propFile = File("../../local.properties")
        val localProperties = Properties()
        if (propFile.exists()) {
            localProperties.load(propFile.inputStream())
        }
        slackToken = if (propFile.exists()) localProperties["slack.token"].toString() else ""
        slackChannelId = if (propFile.exists()) localProperties["slack.channelId"].toString() else ""
    }

}