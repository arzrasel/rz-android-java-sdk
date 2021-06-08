package com.rzandroid.propermission;

import android.Manifest;

public class PermissionRequest {
    public static class Code {
        public static final int INTERNET = 10001;
        public static final int LOCATION = 10010;
        public static final int READ_EXTERNAL = 10020;
        public static final int WRITE_EXTERNAL = 10021;
        public static final int READ_PHONE_STATE = 10030;
        public static final int READ_LOGS = 10050;
    }
    public static class Permission {
        public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    }
}
