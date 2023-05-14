package me.xx2bab.buildinaction.slackblocks

import com.android.build.api.variant.BuiltArtifactsLoader
import groovy.lang.Closure
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.IOException

@CacheableTask
abstract class SlackNotificationTask : DefaultTask() {

    @get:Input
    abstract val variantName: Property<String>

    @get:Optional
    @get:Input
    abstract val kotlinChannelsByVariantSelector: Property<ChannelsByVariantSelector>

    @get:Optional
    @get:Input
    abstract val groovyChannelsByVariantSelector: Property<Closure<Boolean>>

    @get:Input
    var channels: NamedDomainObjectContainer<SlackChannel>? = null

    @get:Input
    abstract val defaultMessage: Property<String>

    @get:Internal
    abstract val builtArtifactsLoader: Property<BuiltArtifactsLoader>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val apkFolder: DirectoryProperty

    @get:OutputFile
    abstract val notifyPayloadLog: RegularFileProperty

    @TaskAction
    fun notifyBuildCompletion() {
        val builtArtifacts = builtArtifactsLoader.get().load(apkFolder.get())
            ?: throw RuntimeException("Cannot load APKs")
        val allArtifactPaths = builtArtifacts.elements.map {
            it.outputFile
        }

        project.logger.lifecycle("[Pg][Slack]: Build completed, posting results...")

        val baseContent = StringBuilder()
            .append("Result: OK\n")
            .append("Variant: ${variantName.get()}\n")
            .append("Outputs: ")
        allArtifactPaths.forEach {
            baseContent.append(it)
                .append("\n")
        }

        channels!!.forEach { slackChannel ->
            if (!isTheChannelRequiredByVariant(
                    variantName.get(),
                    slackChannel.name
                )
            ) {
                return@forEach
            }

            project.logger.lifecycle("[Pg][Slack]: sending to ${slackChannel.name}")

            val customMsg = slackChannel.channelMsg.get().ifBlank {
                defaultMessage.get()
            } + "\n $baseContent"
            val (code, responseBody) = postOnSlack(
                slackChannel.token.get(),
                slackChannel.channelId.get(),
                customMsg
            )
            logToFile(
                slackChannel.name,
                customMsg,
                responseBody,
                code
            )

            project.logger.lifecycle("[Pg][Slack]:$responseBody")
        }
        project.logger.lifecycle("[Pg][Slack]: $name Done")
    }

    private fun isTheChannelRequiredByVariant(
        variantName: String,
        channelName: String
    ) = when {
        kotlinChannelsByVariantSelector.isPresent -> {
            kotlinChannelsByVariantSelector.get()
                .invoke(variantName, channelName)
        }
        groovyChannelsByVariantSelector.isPresent -> {
            groovyChannelsByVariantSelector.get()
                .call(variantName, channelName)
        }
        else -> true
    }

    private fun postOnSlack(
        token: String,
        channel: String,
        text: String
    ): Pair<Int, String> {
        val jsonWithUtf8 = "application/json; charset=utf-8".toMediaType()
        val requestBody = ("{\"channel\": \"$channel\", "
                + "\"text\": \"$text\"}")
        val request: Request = Request.Builder()
            .url("https://slack.com/api/chat.postMessage")
            .header("Authorization", "Bearer $token")
            .post(requestBody.toRequestBody(jsonWithUtf8))
            .build()
        var code: Int
        var responseBody: String
        try {
            OkHttpClient().newCall(request)
                .execute()
                .use { response ->
                    code = response.code
                    responseBody = response.body?.string() ?: ""
                }
        } catch (e: IOException) {
            code = -1
            responseBody = ""
        }
        return Pair(code, responseBody)
    }

    private fun logToFile(
        slackChannelName: String,
        requestBody: String,
        responseBody: String,
        responseCode: Int
    ) {
        if (notifyPayloadLog.isPresent) {
            notifyPayloadLog.get().asFile.appendText(
                """
                |requestOf = $slackChannelName
                |requestBody = $requestBody
                |responseOf = $slackChannelName
                |responseCode = $responseCode
                |responseBody = $responseBody                        
                """.trimMargin()
            )
        }
    }

}