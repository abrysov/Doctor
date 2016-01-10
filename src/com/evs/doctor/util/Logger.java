package com.evs.doctor.util;

import android.util.Log;

public class Logger {
    private static final String APP_TAG = "com.evs.doctor";

    public static void w(Class<?> clazz, String msg) {
        w(clazz, msg, null);
    }

    public static void e(Class<?> clazz, String msg) {
        e(clazz, msg, null);
    }

    public static void e(Class<?> clazz, Throwable tr) {
        e(clazz, clazz.getSimpleName() + " : " + tr.getMessage(), tr);
    }

    /** System calls */
    public static void e(Class<?> clazz, String msg, Throwable th) {
        if (isLoggable(Log.ERROR)) {
            Log.e(APP_TAG, clazz.getSimpleName() + " : " + msg, th);
            log(clazz, msg, th);
        }
    }

    public static void w(Class<?> clazz, String msg, Throwable tr) {
        if (isLoggable(Log.WARN)) {
            Log.w(APP_TAG, clazz.getSimpleName() + " : " + msg, tr);
        }
    }

    public static void i(Class<?> clazz, String msg) {
        if (isLoggable(Log.INFO)) {
            Log.i(APP_TAG, clazz.getSimpleName() + " : " + msg);
        }
    }

    public static void d(Class<?> clazz, String msg) {
        if (isLoggable(Log.DEBUG)) {
            Log.d(APP_TAG, clazz.getSimpleName() + " : " + msg);
        }
    }

    public static void log(Class<?> clazz, String msg, Throwable th) {
        Log.i(APP_TAG, clazz.getSimpleName() + " : " + msg);
        if (!AppConfig.isDevMode()) {
            // Crittercism.logHandledException(th);
        }
    }

    /**
     * @param level
     * @return
     */
    public static boolean isLoggable(int level) {
        // if (Log.DEBUG == level) {
        // return false;
        // }
        return true;// && Log.isLoggable(APP_TAG, level);
    }
}
