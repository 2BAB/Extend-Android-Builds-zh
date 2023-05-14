package me.xx2bab.buildinaction.slacklazy

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

abstract class SlackNotificationPlugin : Plugin<Project> {

    private val androidAppPluginApplied = AtomicBoolean(false)

    override fun apply(project: Project) {
        // Create Extension whatever the safe check is passed or not later,
        // because we need to support the safe-accessor generation
        // which enables IDE auto-complete feature for Gradle Kotlin DSL Script.
        val slackExtension = project.extensions.create(
            "slackNotification1",
            SlackNotificationExtension::class.java
        ).apply {
            enabled.convention(true)
            message.convention("")
        }

        // Now we usually do a safe check of applied place.
        // Can not use this approach because when IDE triggers Gradle Sync,
        // if the plugin config is separated from app's build script (for example we put
        // slack-config.gradle.kts in /build-config module),
        // it will show AppPlugin is not applied.
        //    if (!project.plugins.hasPlugin(AppPlugin::class.java)) {
        //        throw IllegalStateException("Required Android AppPlugin.")
        //    }
        // Use below instead.
        project.afterEvaluate {
            check(androidAppPluginApplied.get()) {
                "Slack notification plugin should only be applied to an Android Application project " +
                        "but ${project.displayName} doesn't have the 'com.android.application' plugin applied."
            }
        }

        // `withType<>{}` is null-safe, makes sure if the AppPlugin is not found,
        // the plugin doesn't go through the rest procedure.
        project.plugins.withType<AppPlugin> {
            androidAppPluginApplied.set(true)

            // Main logic starts from here.
            val androidExtension =
                project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
            androidExtension.onVariants { appVariant ->
                // 1. Precheck.
                if (slackExtension.channelId.get().isBlank()
                    || slackExtension.token.get().isBlank()
                ) {
                    throw IllegalArgumentException(
                        "Please specify target Slack Channel and Token " +
                                "in \"slackNotification{}\" block."
                    )
                }
                if (slackExtension.enabled.get()) {
                    // 2. More arguments.
                    val artifactsLoader = appVariant.artifacts.getBuiltArtifactsLoader()
                    val apkDir = appVariant.artifacts.get(SingleArtifact.APK)
                    val logFile = project.layout
                        .buildDirectory
                        .dir("outputs/logs/")
                        .map {
                            it.file("slack-notification.log")
                        }
                    //  3. Task registry.
                    project.tasks.register(
                        "assembleAndNotify${appVariant.name.capitalize(Locale.ENGLISH)}",
                        SlackNotificationTask::class.java
                    ) {
                        token.set(slackExtension.token)
                        channelId.set(slackExtension.channelId)
                        message.set(slackExtension.message)
                        builtArtifactsLoader.set(artifactsLoader)
                        apkFolder.set(apkDir)
                        notifyPayloadLog.set(logFile)
                    }
                }
            }
        }
    }

}

