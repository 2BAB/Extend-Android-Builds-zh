package me.xx2bab.buildinaction.slack

import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty

@Suppress("UnstableApiUsage", "unused")
abstract class SlackNotificationExtension {

    var enabled = true

    var token = ""

    var channelId = ""

    var message = ""

}