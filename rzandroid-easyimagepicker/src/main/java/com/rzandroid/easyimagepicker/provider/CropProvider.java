package com.rzandroid.easyimagepicker.provider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.rzandroid.easyimagepicker.EasyImagePicker;
import com.rzandroid.easyimagepicker.ImagePickerActivity;
import com.rzandroid.easyimagepicker.R;
import com.rzandroid.easyimagepicker.util.FileUtil;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class CropProvider extends BaseProvider {
    private ImagePickerActivity activity;
    private int maxWidth;
    private int maxHeight;
    private boolean isCrop;
    private float cropAspectX;
    private float cropAspectY;
    private File cropImageFile;
    private File fileDir;
    private static String TAG = CropProvider.class.getSimpleName();
    private static String STATE_CROP_FILE = "state.crop_file";

    public CropProvider(ImagePickerActivity argActivity) {
        super(argActivity);
        activity = argActivity;
        Intent intent = activity.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        // Get Max Width/Height parameter from Intent
        maxWidth = bundle.getInt(EasyImagePicker.EXTRA_MAX_WIDTH, 0);
        maxHeight = bundle.getInt(EasyImagePicker.EXTRA_MAX_HEIGHT, 0);
        // Get Crop Aspect Ratio parameter from Intent
        isCrop = bundle.getBoolean(EasyImagePicker.EXTRA_CROP, false);
        cropAspectX = bundle.getFloat(EasyImagePicker.EXTRA_CROP_X, 0.0F);
        cropAspectY = bundle.getFloat(EasyImagePicker.EXTRA_CROP_Y, 0.0F);
        // Get File Directory
        String fileDir = bundle.getString(EasyImagePicker.EXTRA_SAVE_DIRECTORY);
        this.fileDir = this.getFileDir(fileDir);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_CROP_FILE, cropImageFile);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore crop file
        cropImageFile = (File) (savedInstanceState != null ? savedInstanceState.getSerializable(STATE_CROP_FILE) : null);
    }

    public final boolean isCropEnabled() {
        return isCrop;
    }

    public void startIntent(Uri argUri) throws IOException {
        cropImage(argUri);
    }

    private void cropImage(Uri argUri) throws IOException {
        String extension = FileUtil.getImageExtension(argUri);
        cropImageFile = FileUtil.getImageFile(fileDir, extension);
        if (cropImageFile == null || !cropImageFile.exists()) {
            //Log.e(TAG, "Failed to create crop image file");
            setError(R.string.error_failed_to_crop_image);
            return;
        }
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(FileUtil.getCompressFormat(extension));
        //
        UCrop uCrop = UCrop.of(argUri, Uri.fromFile(cropImageFile))
                .withOptions(options);
        if (cropAspectX > (float) 0 && cropAspectY > (float) 0) {
            uCrop.withAspectRatio(cropAspectX, cropAspectY);
        }

        if (maxWidth > 0 && maxHeight > 0) {
            uCrop.withMaxResultSize(maxWidth, maxHeight);
        }
        try {
            uCrop.start(activity, UCrop.REQUEST_CROP);
        } catch (ActivityNotFoundException ex) {
            setError("uCrop not specified in manifest file." +
                    "Add UCropActivity in Manifest" +
                    "<activity\n" +
                    "    android:name=\"com.yalantis.ucrop.UCropActivity\"\n" +
                    "    android:screenOrientation=\"portrait\"\n" +
                    "    android:theme=\"@style/Theme.AppCompat.Light.NoActionBar\"/>"
            );
            ex.printStackTrace();
        }
    }

    public void onActivityResult(int argRequestCode, int argResultCode, Intent argData) {
        if (argRequestCode == 69) {
            if (argResultCode == -1) {
                handleResult(cropImageFile);
            } else {
                setResultCancel();
            }
        }
    }

    private void handleResult(File argFile) {
        if (argFile != null) {
            activity.setCropImage(Uri.fromFile(argFile));
        } else {
            setError(R.string.error_failed_to_crop_image);
        }
    }

    protected void onFailure() {
        delete();
    }

    public final void delete() {
        cropImageFile.delete();
        cropImageFile = null;
    }
}
