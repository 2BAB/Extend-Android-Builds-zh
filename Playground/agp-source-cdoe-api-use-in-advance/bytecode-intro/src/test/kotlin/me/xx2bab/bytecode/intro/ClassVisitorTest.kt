package me.xx2bab.bytecode.intro

import android.os.Bundle
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test

class ClassVisitorTest {

    private val testActivity = SourceFile.kotlin(
        "TestActivity.kt", """
            package me.xx2bab.activity
            import android.os.Bundle
            import android.os.PowerManager
            import android.os.PowerManager.WakeLock
            import me.xx2bab.bytecode.intro.Logger
            import me.xx2bab.bytecode.intro.LogCollector

            class TestActivity {
                var message = ""
            
                init {
                    Logger.registerCollector{ message -> this@TestActivity.message = message }
                }
                            
                fun acquireWakeLock() {
                    val wakeLock = PowerManager().newWakeLock(0, "tag")
                    wakeLock.acquire()                   
                }

                fun onCreate(bundle: Bundle) {                                     
                    acquireWakeLock()                
                }

            }
        """.trimIndent()
    )


    @Test
    fun `Replaced WakeLock in TestActivity with WakeLockProxy Successfully`() {
        val result = KotlinCompilation().apply {
            sources = listOf(testActivity)
            inheritClassPath = true
            messageOutputStream = System.out // To see diagnostics in real time
        }.compile()
        result.printAll()

        // Asserts 1: original file compilation should be ok
        assertThat(result.exitCode, `is`(KotlinCompilation.ExitCode.OK))

        val compiledTestClassFile = result.getCompiledFileByName("TestActivity.class")
        val asmManipulator = ASMManipulator(
            inputClassFile = compiledTestClassFile,
        )
        val classVisitor = ReplaceClassVisitor(asmManipulator.writer)
        asmManipulator.processInPlace(classVisitor)

        // Asserts 2: ByteCode integrity check
        val errorLog = compiledTestClassFile.checkByteCodeIntegrity()
        assertThat(
            "The revised class file does not pass the integrity check, the reason is:\n $errorLog",
            errorLog == null
        )

        val modifiedTestActivity = result.classLoader.loadClass("me.xx2bab.activity.TestActivity")
        val testActivityInstance = modifiedTestActivity.getDeclaredConstructor().newInstance()
        testActivityInstance.invokeMethod(modifiedTestActivity, "acquireWakeLock", emptyList())

        // Asserts 3: modified bytecodes work as expected
        val message = testActivityInstance.getFieldValueInString(modifiedTestActivity, "message")
        assertThat(message, `is`("WakeLockProxy#acquire"))
    }

    @Test
    fun `Inserted a log in TestActivity#onCreate() Successfully`() {
        val result = KotlinCompilation().apply {
            sources = listOf(testActivity)
            inheritClassPath = true
            messageOutputStream = System.out // To see diagnostics in real time
        }.compile()
        result.printAll()

        // Asserts 1: original file compilation should be ok
        assertThat(result.exitCode, `is`(KotlinCompilation.ExitCode.OK))

        val compiledTestClassFile = result.getCompiledFileByName("TestActivity.class")
        val asmManipulator = ASMManipulator(
            inputClassFile = compiledTestClassFile,
        )
        val classVisitor = OnCreateClassVisitor(asmManipulator.writer)
        asmManipulator.processInPlace(classVisitor)

        // Asserts 2: ByteCode integrity check
        val errorLog = compiledTestClassFile.checkByteCodeIntegrity()
        assertThat(
            "The revised class file does not pass the integrity check, the reason is:\n $errorLog",
            errorLog == null
        )

        val modifiedTestActivity = result.classLoader.loadClass("me.xx2bab.activity.TestActivity")
        val testActivityInstance = modifiedTestActivity.getDeclaredConstructor().newInstance()
        testActivityInstance.invokeMethod(modifiedTestActivity, "onCreate", listOf(Bundle::class.java), Bundle())

        // Asserts 3: modified bytecodes work as expected
        val message = testActivityInstance.getFieldValueInString(modifiedTestActivity, "message")
        assertThat(message, `is`("me/xx2bab/activity/TestActivity#onCreate"))
    }

}