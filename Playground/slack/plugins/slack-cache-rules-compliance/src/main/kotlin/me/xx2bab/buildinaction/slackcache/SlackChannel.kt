package me.xx2bab.buildinaction.slackcache

import org.gradle.api.Named
import org.gradle.api.provider.Property

interface SlackChannel: Named {

    val token: Property<String>

    val channelId: Property<String>

    val channelMsg: Property<String>

}