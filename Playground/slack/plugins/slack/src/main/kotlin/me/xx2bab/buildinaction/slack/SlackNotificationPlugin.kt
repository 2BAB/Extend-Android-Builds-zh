package me.xx2bab.buildinaction.slack

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

abstract class SlackNotificationPlugin : Plugin<Project> {

    private val androidAppPluginApplied = AtomicBoolean(false)

    override fun apply(project: Project) {
        // Create Extension whatever the safe check is passed or not later,
        // because we need to support the safe-accessor generation
        // which enables IDE auto-complete feature for Gradle Kotlin DSL Script.
        val slackExtension = project.extensions.create<SlackNotificationExtension>(
            "slackNotification"
        )
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
            // println("channelID: ${slackExtension.channelId}")
        }
        // `withType<>{}` is null-safe, makes sure if the AppPlugin is not found,
        // the plugin doesn't go through the rest procedure.
        project.plugins.withType<AppPlugin> {
            androidAppPluginApplied.set(true)

            // Main logic starts from here.
            val androidExtension = project.extensions.findByType(AppExtension::class.java)!!
            androidExtension.applicationVariants.configureEach {
                // 1. Precheck.
                if (slackExtension.channelId.isBlank()
                    || slackExtension.token.isBlank()
                ) {
                    throw IllegalArgumentException(
                        "Please specify target Slack Channel and Token " +
                                "in \"slackNotification{}\" block."
                    )
                }
                if (slackExtension.enabled) {
                    val appVariant = this

                    //  2. Task registry.
                    val taskProvider = project.tasks.register<SlackNotificationTask>(
                        "assembleAndNotify${appVariant.name.capitalize()}"
                    ) {
                        token = slackExtension.token
                        channelId = slackExtension.channelId
                        message = slackExtension.message
                        notifyPayloadLog = File(
                            appVariant.outputs.first().outputFile.parent,
                            "slack-notification.log"
                        )
                    }
                    taskProvider.dependsOn(this.assembleProvider)
                }
            }
        }
    }

}
