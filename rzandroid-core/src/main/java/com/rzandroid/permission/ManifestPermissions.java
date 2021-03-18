package com.rzandroid.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
//import androidx.core.app.ActivityCompat;

public class ManifestPermissions {
    private Context context;
    private static ManifestPermissions instance = null;
    private OnEventListenerHandler onEventListenerHandler;
    //int[] intArray = { 1,2,3,4,5,6,7,8,9,10 };
    //int[] intArray = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
    /*private final int REQUEST_LOCATION_CODE = 9999;
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };*/
    //int arr[][] = { {2,7,9},{3,6,1},{7,4,2} };
    public static final int REQUEST_CODE_INTERNET = 10001;
    public static final int REQUEST_CODE_LOCATION = 10010;
    public static final int REQUEST_CODE_READ_EXTERNAL = 10020;
    public static final int REQUEST_CODE_WRITE_EXTERNAL = 10021;
    public static final int REQUEST_CODE_READ_PHONE_STATE = 10030;
    public static final int READ_LOGS = 10050;

    public static ManifestPermissions getInstance(Context argContext) {
        if (instance == null) {
            instance = new ManifestPermissions(argContext);
        }
        return instance;
    }

    public ManifestPermissions(Context argContext) {
        context = argContext;
    }

    public void onSetEventListener(OnEventListenerHandler argOnEventListenerHandler) {
        onEventListenerHandler = argOnEventListenerHandler;
    }

    /**
     * Construct a new permission request builder with a host, request code, and the requested
     * permissions.
     *
     * @param argPermissions the set of permissions to be requested
     */
//    public boolean hasPermission(@NonNull @Size(min = 1) String... argPermissions) {
    public boolean hasPermission(String... argPermissions) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : argPermissions) {
                if (ActivityCompat.checkSelfPermission(argContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        String tagData = "MANIFEST PERMISSION NEEDED";
        int tagAtMostCharacters = 23;
        String upToNCharacters = tagData.substring(0, Math.min(tagData.length(), tagAtMostCharacters));
        String tag = upToNCharacters.toUpperCase();
        /*for (String permission : argPermissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e(tag, permission);
                return false;
            }
        }*/
        return true;
    }

    public void onRequestPermissions(int argPermissionRequestCode, String... argPermissions) {
        //if (str instanceof String == false)
        /*if (!(argPermissions instanceof String[])) {
        }*/
        //System.out.println("CALLED===========================================onRequestPermissions");
//        ActivityCompat.requestPermissions((Activity) context, argPermissions, argPermissionRequestCode);
    }

    public void onRequestPermissionsResult(int argRequestCode, String argPermissions[], int[] argGrantResults, int argPermissionRequestCode) {
        //if(Build.VERSION.SDK_INT==Build.VERSION_CODES.M)
        //if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        //LogWriter.Log("REQUEST_CODE: " + argRequestCode + " PERISSION_REQUEST_CODE: " + argPermissionRequestCode + " REQUEST_STRINGS: " + argPermissions.toString());
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //System.out.println("CALLED===========================================onRequestPermissionsResult");
        //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================" + argRequestCode);
        //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================" + argPermissionRequestCode);
        if (argRequestCode == argPermissionRequestCode || READ_LOGS == argPermissionRequestCode) {
            //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================inside argPermissionRequestCode");
            if (argGrantResults.length > 0 && argGrantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! do the
                // calendar task you need to do.
                //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================inside matched");
                if (onEventListenerHandler != null) {
                    //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================true");
                    onEventListenerHandler.onPermissionGranted(true);
                }
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================inside not matched");
                if (onEventListenerHandler != null) {
                    //System.out.println("ERROR_SUCCESS_DEBUG_RESPONSE===========================================false");
                    onEventListenerHandler.onPermissionGranted(false);
                }
            }
            return;
        }
        // other 'switch' lines to check for other
        // permissions this app might request
    }

    public interface OnEventListenerHandler {
        public void onPermissionGranted(boolean argIsGranted);
        //public void onPermissionForbidden();
    }

    private void logWriter(String argMessage) {
        System.out.println("DEBUG_LOG_" + argMessage);
        Log.e("DEBUG_LOG", argMessage);
    }
}
//https://github.com/googlesamples/easypermissions
//https://github.com/tbruyelle/RxPermissions
//https://medium.com/mindorks/multiple-runtime-permissions-in-android-without-any-third-party-libraries-53ccf7550d0