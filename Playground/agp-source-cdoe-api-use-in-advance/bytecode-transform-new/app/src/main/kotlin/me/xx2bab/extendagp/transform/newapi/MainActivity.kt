package me.xx2bab.extendagp.transform.newapi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.widget.Button
import android.widget.TextView
import me.xx2bab.bytecode.intro.Logger

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logTextView = findViewById<TextView>(R.id.log)
        Logger.registerCollector { message ->
            logTextView.text = message
        }

        val wakeLock: PowerManager.WakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag")
            }

        findViewById<Button>(R.id.trigger_acquire).setOnClickListener {
            wakeLock.acquire()
        }
        findViewById<Button>(R.id.trigger_release).setOnClickListener {
            wakeLock.release()
        }
    }
}