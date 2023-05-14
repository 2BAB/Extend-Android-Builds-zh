package me.xx2bab.bytecode.intro;

import android.os.PowerManager;
import android.util.Log;

public class WakeLockProxy {

    public static void acquire(PowerManager.WakeLock wakelock) {
        System.out.println("WakeLockProxy acquire clicked");
        wakelock.acquire();
        Logger.log("WakeLockProxy#acquire");
        Log.i("WakeLockProxy", "acquire");
    }

    public static void acquire(PowerManager.WakeLock wakelock, long timeout) {
        wakelock.acquire(timeout);
        Logger.log("WakeLockProxy#acquire with timeout: " + timeout);
        Log.i("WakeLockProxy", "acquire");
    }

    public static void release(PowerManager.WakeLock wakelock) {
        wakelock.release();
        Logger.log("WakeLockProxy#release");
        Log.i("WakeLockProxy", "release");
    }

    public static void release(PowerManager.WakeLock wakelock, int flags) {
        wakelock.release(flags);
        Logger.log("WakeLockProxy#release with flags: " + flags);
        Log.i("WakeLockProxy", "release");
    }

}