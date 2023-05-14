package me.xx2bab.extendagp.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.build.event.BuildEventsListenerRegistry
import org.gradle.configurationcache.extensions.get
import org.gradle.initialization.DefaultSettings
import org.gradle.internal.operations.notify.*
import org.gradle.kotlin.dsl.registerIfAbsent
import javax.inject.Inject

class BuildEventsListenerRegistryPlugin @Inject constructor(
    private val buildEventsListenerRegistry: BuildEventsListenerRegistry
) : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.gradle.run {
            val buildFinishTrackService = sharedServices.registerIfAbsent(
                "buildFinishTrackService", BuildFinishTrackService::class) {
                parameters {}
            }
            buildEventsListenerRegistry.onTaskCompletion(buildFinishTrackService)
        }
    }
}
