package me.xx2bab.buildinaction.slacktest

import groovy.lang.Closure
import me.xx2bab.buildinaction.slacktest.model.DefaultConfig
import me.xx2bab.buildinaction.slacktest.model.SlackChannel
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class SlackNotificationExtension @Inject constructor(
    objects: ObjectFactory
) {

    abstract val enabled: Property<Boolean>

    abstract val channels: NamedDomainObjectContainer<SlackChannel>


    val defaultConfig: DefaultConfig = objects.newInstance(
        DefaultConfig::class.java
    )

    fun defaultConfig(action: Action<DefaultConfig>) {
        // println("defaultConfig is invoked with Action")
        action.execute(defaultConfig)
    }

    fun defaultConfig(action: DefaultConfig.() -> Unit) {
        // println("defaultConfig is invoked with Kotlin Type-Safe builders (function type with receiver).")
        action.invoke(defaultConfig)
    }


    internal abstract val kotlinChannelsByVariantSelector: Property<ChannelsByVariantSelector>

    internal abstract val groovyChannelsByVariantSelector: Property<Closure<Boolean>>

    // For Gradle Kotlin DSL
    fun selectChannelsByVariant(selector: ChannelsByVariantSelector) {
        kotlinChannelsByVariantSelector.set(selector)
    }

    // For Gradle Groovy DSL
    fun selectChannelsByVariant(selector: Closure<Boolean>) {
        groovyChannelsByVariantSelector.set(selector.dehydrate())
    }

}

internal typealias ChannelsByVariantSelector = (variantName: String, channelName: String) -> Boolean