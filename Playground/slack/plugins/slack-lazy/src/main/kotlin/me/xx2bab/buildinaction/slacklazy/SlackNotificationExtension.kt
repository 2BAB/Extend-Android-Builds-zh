package me.xx2bab.buildinaction.slacklazy

import org.gradle.api.provider.Property

@Suppress("UnstableApiUsage", "unused")
interface SlackNotificationExtension {

    val enabled: Property<Boolean>

    val token: Property<String>

    val channelId: Property<String>

    val message: Property<String>

}