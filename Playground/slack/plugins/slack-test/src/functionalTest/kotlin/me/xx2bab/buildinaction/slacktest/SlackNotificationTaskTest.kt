package me.xx2bab.buildinaction.slacktest

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.envProps
import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.execute
import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.executeExpectingFailure
import me.xx2bab.buildinaction.slacktest.base.SlackConfigs
import org.gradle.testkit.runner.TaskOutcome
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class SlackNotificationTaskTest {

    @Test
    fun `Assemble and Notify the Slack Bot successfully`() {
        val taskPath = ":app:assembleAndNotifyDebug"
        val result = execute(
            SlackConfigs.regular(envProps.slackToken, envProps.slackChannelId),
            taskPath
        )
        assertThat(
            "Slack Notify Task should work well, but threw unknown errors.",
            result.buildResult.task(taskPath)?.outcome != TaskOutcome.FAILED
        )
        val csvFile = File(result.projectDir, "app/build/outputs/logs/slack-notification.csv")
        val csvData: List<Map<String, String>> = csvReader().readAllWithHeader(csvFile.readText())
        for (data in csvData) {
            assertThat(
                "${data["requestOf"]} request is not successful",
                data["responseCode"]!!.toBoolean()
            )
        }
    }

    @Test
    fun `Build failed due to the SlackNotification is not configurated`() {
        val taskPath = ":app:assembleAndNotifyDebug"
        val result = executeExpectingFailure(
            SlackConfigs.partialConfiguratedOnly,
            taskPath
        )
        assertThat(
            "Extension configuration is not completed but somehow the build worked...",
            result.buildResult.task(taskPath) == null
        )
    }

}