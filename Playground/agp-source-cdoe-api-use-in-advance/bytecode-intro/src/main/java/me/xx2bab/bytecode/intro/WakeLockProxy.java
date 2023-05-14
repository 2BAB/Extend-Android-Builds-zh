package me.xx2bab.bytecode.intro;

import android.os.PowerManager;

public class WakeLockProxy {

    public static void acquire(PowerManager.WakeLock wakelock) {
        wakelock.acquire();
        Logger.log("WakeLockProxy#acquire");
    }

    public static void acquire(PowerManager.WakeLock wakelock, long timeout) {
        wakelock.acquire(timeout);
        Logger.log("WakeLockProxy#acquire with timeout: " + timeout);
    }

    public static void release(PowerManager.WakeLock wakelock) {
        wakelock.release();
        Logger.log("WakeLockProxy#release");
    }

    public static void release(PowerManager.WakeLock wakelock, int flags) {
        wakelock.release(flags);
        Logger.log("WakeLockProxy#release with flags: " + flags);
    }

}