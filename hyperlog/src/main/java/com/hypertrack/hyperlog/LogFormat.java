/*
The MIT License (MIT)

Copyright (c) 2015-2017 HyperTrack (http://hypertrack.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.hypertrack.hyperlog;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.hypertrack.hyperlog.utils.HLDateTimeUtility;

import java.io.Serializable;
import java.util.UUID;


/**
 * Created by Aman on 10/10/17.
 */

/**
 * This class can be overridden to customise the log message format.
 * <br>
 * An instance of LogFormat needs to be passed to the method
 * {@link HyperLog#setLogFormat(LogFormat)} as parameter.
 */
public class LogFormat implements Serializable {

    private final String deviceUUID;

    public LogFormat(Context context) {
        Context mContext = context.getApplicationContext();
        String uuid = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceUUID = uuid != null ? uuid : "Random"+UUID.randomUUID().toString();
    }

    /**
     * Implement this method to override the default log message format.
     *
     * @param logLevel The level of logcat logging that Parse should do.
     * @param message  Log message that need to be customized.
     * @return Formatted Log Message that will store in database.
     */
    String formatLogMessage(int logLevel, String tag, String message) {
        String timeStamp = HLDateTimeUtility.getCurrentTime();
        String senderName = BuildConfig.VERSION_NAME;
        String osVersion = "Android-" + Build.VERSION.RELEASE;
        String logLevelName = getLogLevelName(logLevel);
        return getFormattedLogMessage(logLevelName, tag, message, timeStamp, senderName, osVersion, deviceUUID);
    }

    public String getFormattedLogMessage(String logLevelName, String tag, String message, String timeStamp,
                                         String senderName, String osVersion, String deviceUUID) {
        LogData data = new LogData(logLevelName, tag, message, timeStamp, senderName, osVersion, deviceUUID);
        return new Gson().toJson(data);
    }

    private static String getLogLevelName(int messageLogLevel) {

        String logLevelName;
        switch (messageLogLevel) {
            case Log.VERBOSE:
                logLevelName = "VERBOSE";
                break;
            case Log.DEBUG:
                logLevelName = "DEBUG";
                break;
            case Log.INFO:
                logLevelName = "INFO";
                break;
            case Log.WARN:
                logLevelName = "WARN";
                break;
            case Log.ERROR:
                logLevelName = "ERROR";
                break;
            case Log.ASSERT:
                logLevelName = "ASSERT";
                break;
            default:
                logLevelName = "NONE";
        }

        return logLevelName;
    }

}
