package me.xx2bab.buildinaction.slacktest

import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.envProps
import me.xx2bab.buildinaction.slacktest.base.GradleRunnerExecutor.execute
import me.xx2bab.buildinaction.slacktest.base.SlackConfigs
import org.gradle.testkit.runner.TaskOutcome
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class SourceToVerificationCodesTaskTest {

    @Test
    fun `SourceToVerificationCodesTask should work in debug variant`() {
        val taskPath = ":app:debugSourceToVerificationCodes"
        val result = execute(
            SlackConfigs.regular(envProps.slackToken, envProps.slackChannelId),
            taskPath
        )
        assertThat(
            "Source to verification codes failed.",
            result.buildResult.task(taskPath)?.outcome != TaskOutcome.FAILED
        )
        val dummyFileVerification = File(result.projectDir,
            "app/build/src_verification/java_Dummy.java_SHA256MD5.properties")
        val props = Properties().apply {
            load(dummyFileVerification.inputStream())
        }
        assertThat("Dummy.java SHA256 changed.", props["SHA256"] == "176a11aa89e7e8372b545a81d4cf18de8525f5a7fc2cffb53d3cb865015af279")
        assertThat("Dummy.java MD5 changed.", props["MD5"] == "0d6e408e66575b51adb0daab21c95c68")
    }

}