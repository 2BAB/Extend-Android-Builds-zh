package me.xx2bab.buildinaction.slacktest

import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.envProps
import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.execute
import me.xx2bab.buildinaction.slacktest.base.SlackConfigs
import org.gradle.testkit.runner.TaskOutcome
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class ProjectIntegrityTest {

    @Test
    fun `Check Gradle scripts integrity`() {
        val taskPath = ":app:clean"
        val result = execute(
            SlackConfigs.regular(envProps.slackToken, envProps.slackChannelId),
            taskPath
        )
        assertThat(
            "Project gradle scripts may have some compilation errors" +
                    " that causes the clean task failed.",
            result.buildResult.task(taskPath)?.outcome != TaskOutcome.FAILED
        )
    }

    @Test
    fun `Check project integrity by assembling`() {
        val taskPath = ":app:assembleDebug"
        val result = execute(
            SlackConfigs.regular(envProps.slackToken, envProps.slackChannelId),
            taskPath
        )
        assertThat(
            "Project gradle scripts may have some compilation errors" +
                    " that causes the clean task failed.",
            result.buildResult.task(taskPath)?.outcome != TaskOutcome.FAILED
        )
    }

}