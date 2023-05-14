package me.xx2bab.extendagp.transform.newapi

import android.widget.Button
import me.xx2bab.bytecode.intro.Logger
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// It's working for Instrumentation API test only,
// the Variant API Task of Transforming bytecodes is not supported yet of AGP in unit test builds.
@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    @Config(sdk = [28])
    fun `MainActivity should invoke WakeLockProxy#acquire`() {
        Robolectric.buildActivity(MainActivity::class.java).use { controller ->
            controller.setup() // Moves Activity to RESUMED state
            val activity: MainActivity = controller.get()
            val triggerAcquire = activity.findViewById<Button>(R.id.trigger_acquire)
            val triggerRelease = activity.findViewById<Button>(R.id.trigger_release)
            var logReplica = ""
            Logger.registerCollector { message ->
                logReplica = message
            }
            triggerAcquire.performClick()

            Thread.sleep(100)
            assertThat(logReplica, `is`("WakeLockProxy#acquire"))
        }
    }

}