
package com.ll.filebrowser.util;

import android.util.Log;

/**
 * @Description Logger to ConnectAPN project,instead of android.util.Log
 * @author damon
 * @date Aug 2, 2013 4:05:58 PM
 * @version V1.3.1
 */
public class Logger {
    /**
     * Priority constant for the prbooleanln method; use Log.v.
     */
    private static final boolean ENABLE_VERBOSE = true;

    /**
     * Priority constant for the prbooleanln method; use Log.d.
     */
    private static final boolean ENABLE_DEBUG = true;

    /**
     * Priority constant for the prbooleanln method; use Log.i.
     */
    private static final boolean ENABLE_INFO = true;

    /**
     * Priority constant for the prbooleanln method; use Log.w.
     */
    private static final boolean ENABLE_WARN = true;

    /**
     * Priority constant for the prbooleanln method; use Log.e.
     */
    private static final boolean ENABLE_ERROR = true;


    public static void v(String tag, String msg) {
        if (ENABLE_VERBOSE)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (ENABLE_DEBUG)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (ENABLE_ERROR)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (ENABLE_WARN)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (ENABLE_INFO)
            Log.i(tag, msg);
    }

}
