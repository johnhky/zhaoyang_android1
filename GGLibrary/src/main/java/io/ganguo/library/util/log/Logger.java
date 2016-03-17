package io.ganguo.library.util.log;

import android.util.Log;

/**
 * 常规日志，输出到Console中
 * <p/>
 * Created by Tony on 4/4/15.
 */
public class Logger {

    private String mTag;

    public Logger(String tag) {
        mTag = tag;
    }

    public String tag() {
        try {
            Throwable throwable = new Throwable();
            String methodName = throwable.getStackTrace()[4].getMethodName() + "()";
            return LogConfig.TAG_PREFIX + "/" + mTag + "#" + methodName;
        } catch (Exception e) {
            return LogConfig.TAG_PREFIX + "/" + mTag;
        }
    }

    public int v(Object msg) {
        return println(Log.VERBOSE, msg, null);
    }

    public int v(Object msg, Throwable tr) {
        return println(Log.VERBOSE, msg, tr);
    }

    public int d(Object msg) {
        return println(Log.DEBUG, msg, null);
    }

    public int d(Object msg, Throwable tr) {
        return println(Log.DEBUG, msg, tr);
    }

    public int i(Object msg) {
        return println(Log.INFO, msg, null);
    }

    public int i(Object msg, Throwable tr) {
        return println(Log.INFO, msg, tr);
    }

    public int w(Object msg) {
        return println(Log.WARN, msg, null);
    }

    public int w(Object msg, Throwable tr) {
        return println(Log.WARN, msg, tr);
    }

    public int e(Object msg) {
        return println(Log.ERROR, msg, null);
    }

    public int e(Object msg, Throwable tr) {
        return println(Log.ERROR, msg, tr);
    }

    /**
     * 按级别输出日志到Console中
     *
     * @param priority
     * @param msg
     * @param tr
     * @return
     */
    protected int println(int priority, Object msg, Throwable tr) {
        switch (priority) {
            case Log.VERBOSE:
                GLog.v(tag(), msg, tr);
                break;
            case Log.DEBUG:
                GLog.d(tag(), msg, tr);
                break;
            case Log.INFO:
                GLog.i(tag(), msg, tr);
                break;
            case Log.WARN:
                GLog.w(tag(), msg, tr);
                break;
            case Log.ERROR:
                GLog.e(tag(), msg, tr);
                break;
        }
        return 0;
    }

}
