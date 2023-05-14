package me.xx2bab.buildinaction.slackorchestra

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
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import java.io.IOException
import java.net.URI
import javax.inject.Inject

@CacheableTask
abstract class SlackNotificationTask @Inject constructor(
    private val workerExecutor: WorkerExecutor
) : DefaultTask() {

    @get:Internal
    abstract val cloudService: Property<PrivateCloudService>

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
        logger.lifecycle("[Pg][Slack]: Build completed, posting results...")
        logger.lifecycle("[Pg][Slack]: " + cloudService.get().fetch(URI.create("/used/in/another/plugin/and/task")))
        val builtArtifacts = builtArtifactsLoader.get().load(apkFolder.get())
            ?: throw RuntimeException("Cannot load APKs")
        val allArtifactPaths = builtArtifacts.elements.map {
            it.outputFile
        }
        val baseContent = StringBuilder()
            .append("Result: OK\n")
            .append("Variant: ${variantName.get()}\n")
            .append("Outputs: ")
        allArtifactPaths.forEach {
            baseContent.append(it)
                .append("\n")
        }

        val workQueue: WorkQueue = workerExecutor.noIsolation()
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

            workQueue.submit(SlackNotificationWork::class.java) {
                token.set(slackChannel.token)
                channel.set(slackChannel.channelId)
                channelName.set(slackChannel.name)
                message.set(customMsg)
                logFile.set(notifyPayloadLog)
            }
        }
        // If you don't call await(),
        // You will see the "Done" log before http response.
        workQueue.await()
        logger.lifecycle("[Pg][Slack]: $name Done")
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


}

private interface SlackNotificationWorkParam : WorkParameters {
    val token: Property<String>
    val channel: Property<String>
    val channelName: Property<String>
    val message: Property<String>
    val logFile: RegularFileProperty
}

private abstract class SlackNotificationWork // @Inject constructor(private val logger: Logger) :
    : WorkAction<SlackNotificationWorkParam> {

    // The Logger is not yet supported to be a build service injected by built-in DI.
    // https://github.com/gradle/gradle/issues/16991
    // Use below trick as a workaround.
    private val logger = Logging.getLogger(SlackNotificationTask::class.java)

    override fun execute() {
        logger.lifecycle("SlackNotificationWork"  +" " + Thread.currentThread().name)
        val token = parameters.token.get()
        val channel = parameters.channel.get()
        val message = parameters.message.get()
        val channelName = parameters.channelName.get()
        val logFile = parameters.logFile
        val (code, responseBody) = postOnSlack(
            token,
            channel,
            message
        )
        logToFile(
            channelName,
            message,
            responseBody,
            code,
            logFile
        )
        logger.lifecycle("[Pg][Slack]:$responseBody")
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
        channelName: String,
        requestBody: String,
        responseBody: String,
        responseCode: Int,
        logFile: RegularFileProperty
    ) {
        if (logFile.isPresent) {
            logFile.get().asFile.appendText(
                """
                |requestOf = $channelName
                |requestBody = $requestBody
                |responseOf = $channelName
                |responseCode = $responseCode
                |responseBody = $responseBody                        
                """.trimMargin()
            )
        }
    }
}