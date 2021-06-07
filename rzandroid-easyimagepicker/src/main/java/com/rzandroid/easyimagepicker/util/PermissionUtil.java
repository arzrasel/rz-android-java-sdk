package com.rzandroid.easyimagepicker.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public final class PermissionUtil {

    public static boolean isPermissionGranted(Context argContext, String argPermission) {
        //ActivityCompat
        int selfPermission = ContextCompat.checkSelfPermission(argContext, argPermission);
        selfPermission = PackageManager.PERMISSION_GRANTED;
        return selfPermission > 0 ? true : false;
    }

    public static boolean isPermissionGranted(Context argContext, String... argPermission) {
        List<String> permissionNeeded = new ArrayList<>();
        for (String permission : argPermission) {
            if (ActivityCompat.checkSelfPermission(argContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionNeeded.add(permission);
            }
        }
        return permissionNeeded.size() <= 0 ? true : false;
    }

    public static boolean isPermissionInManifest(Context argContext, String argPermission) {
        try {
            PackageInfo packageInfo = argContext.getPackageManager().getPackageInfo(argContext.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions.length <= 0) {
                return false;
            }
            for (String permission : requestedPermissions) {
                if (argContext.equals(permission)) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }
        return false;
    }
}
