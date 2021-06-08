package com.rzandroid.propermission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProPermission {
    private final Activity activity;
    private final OnPermissionListener onPermissionListener;
    private int requestCode = -1;

    private ProPermission(Builder argBuilder) {
        this.activity = argBuilder.activity;
        this.onPermissionListener = argBuilder.listener;
    }

    public void onRequestPermissionsResult(String[] argPermissions, int[] argGrantResults) {
        List<String> grantedPermissions = new ArrayList<>();
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < argGrantResults.length; i++) {
            if (argGrantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(argPermissions[i]);
            } else {
                grantedPermissions.add(argPermissions[i]);
            }
        }
        if (grantedPermissions.size() == argPermissions.length) {
            onPermissionListener.onAllPermissionsGranted(Arrays.asList(argPermissions));
        } else {
            onPermissionListener.onPermissionsGranted(grantedPermissions);
            onPermissionListener.onPermissionsDenied(deniedPermissions);
        }
    }

    public void request(int argRequestCode, String... argPermissions) {
        requestCode = argRequestCode;
        List<String> permissionNeeded = new ArrayList<>();
        for (String permission : argPermissions) {
            if (!hasPermission(permission)) {
                permissionNeeded.add(permission);
            }
        }
        if (permissionNeeded.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionNeeded.toArray(new String[0]), requestCode);
        }
    }

    public boolean hasPermission(String... argPermissions) {
        for (String permission : argPermissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public static class Builder {
        private Activity activity;
        private OnPermissionListener listener;

        public Builder with(Activity argActivity) {
            this.activity = argActivity;
            return this;
        }

        public Builder listener(OnPermissionListener argListener) {
            this.listener = argListener;
            return this;
        }

        public ProPermission build() {
            return new ProPermission(this);
        }
    }
}
