package com.rzandroid.easyimagepicker.provider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.rzandroid.easyimagepicker.EasyImagePicker;
import com.rzandroid.easyimagepicker.ImagePickerActivity;
import com.rzandroid.easyimagepicker.R;
import com.rzandroid.easyimagepicker.util.FileUtil;
import com.rzandroid.easyimagepicker.util.IntentUtils;
import com.rzandroid.easyimagepicker.util.PermissionUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraProvider extends BaseProvider {
    private ImagePickerActivity activity;
    private File cameraFile;
    private File fileDir;
    private static final String STATE_CAMERA_FILE = "state.camera_file";
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_INTENT_REQ_CODE = 4281;
    private static final int PERMISSION_INTENT_REQ_CODE = 4282;

    public CameraProvider(ImagePickerActivity argActivity) {
        super(argActivity);
        activity = argActivity;
        Intent intent = activity.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        // Get File Directory
        String savedFileDir = bundle.getString(EasyImagePicker.EXTRA_SAVE_DIRECTORY);
        fileDir = getFileDir(savedFileDir);
    }

    public void onSaveInstanceState(@NotNull Bundle outState) {
        // Save Camera File
        outState.putSerializable(STATE_CAMERA_FILE, cameraFile);
    }

    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        // Restore Camera File
        cameraFile = (File) savedInstanceState.getSerializable(STATE_CAMERA_FILE);
    }

    public void startIntent() {
        if (!IntentUtils.isCameraAppAvailable((Context) this)) {
            this.setError(R.string.error_camera_app_not_found);
        } else {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (isPermissionGranted(this)) {
            startCameraIntent();
        } else {
            requestPermission();
        }

    }

    private void startCameraIntent() {
        // Create and get empty file to store capture image content
        File file = FileUtil.getImageFile(fileDir, null);
        cameraFile = file;
        //
        if (file != null && file.exists()) {
            Intent cameraIntent = IntentUtils.getCameraIntent(this, file);
            activity.startActivityForResult(cameraIntent, CAMERA_INTENT_REQ_CODE);
        } else {
            this.setError(R.string.error_failed_to_create_camera_image_file);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, getRequiredPermission(activity), PERMISSION_INTENT_REQ_CODE);
    }

    private boolean isPermissionGranted(Context argContext) {
        String[] permissions = getRequiredPermission(argContext);
        for (String permission : permissions) {
            if (!PermissionUtil.isPermissionGranted(argContext, permission)) {
                return false;
            }
        }
        return true;
    }

    private String[] getRequiredPermission(Context argContext) {
        String[] permissions = REQUIRED_PERMISSIONS;
        List<String> list = new ArrayList<String>();
        for (String permission : permissions) {
            if (PermissionUtil.isPermissionInManifest(argContext, permission)) {
                list.add(permission);
            }
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public void onRequestPermissionsResult(int argRequestCode) {
        if (argRequestCode == PERMISSION_INTENT_REQ_CODE) {
            // Check again if permission is granted
            if (isPermissionGranted(this)) {
                // Permission is granted, Start Camera Intent
                startIntent();
            } else {
                // Exit with error message
                String error = getString(R.string.permission_camera_denied);
                setError(error);
            }
        }
    }

    public void onActivityResult(int argRequestCode, int argResultCode, Intent argData) {
        if (argRequestCode == CAMERA_INTENT_REQ_CODE) {
            if (argResultCode == Activity.RESULT_OK) {
                handleResult();
            } else {
                setResultCancel();
            }
        }
    }

    private void handleResult() {
        activity.setImage(Uri.fromFile(cameraFile));
    }

    protected void onFailure() {
        delete();
    }

    public void delete() {
        cameraFile.delete();
        cameraFile = null;
    }

    public static Object[] toArray(List<?> argArray) {
        Object[] arr = new Object[argArray.size()];
        for (int i = 0; i < argArray.size(); i++)
            arr[i] = argArray.get(i);
        return arr;
    }
}
