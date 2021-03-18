package com.rzandroid;

public class RzAndroidSdk {
    //    private static volatile boolean isDebugEnabled = BuildConfig.DEBUG;
    /**
     * Returns the current version of the RzAndroid SDK for Android as a string.
     *
     * @return the current version of the SDK
     */
    public static String getSdkVersion() {
        return RzAndroidSdkVersion.BUILD;
    }
}
