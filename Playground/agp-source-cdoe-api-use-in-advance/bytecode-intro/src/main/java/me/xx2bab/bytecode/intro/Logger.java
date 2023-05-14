package me.xx2bab.bytecode.intro;

import java.util.LinkedList;

public class Logger {

    private static LinkedList<LogCollector> collectors = new LinkedList<>();

    public static void log(String message) {
        for (LogCollector c : collectors) {
            c.collect(message);
        }
    }

    public static void registerCollector(LogCollector c) {
        collectors.add(c);
    }

    public static void unregisterCollector(LogCollector c) {
        collectors.remove(c);
    }

}
