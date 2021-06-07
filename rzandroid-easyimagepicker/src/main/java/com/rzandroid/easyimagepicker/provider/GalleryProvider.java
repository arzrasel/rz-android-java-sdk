package com.rzandroid.easyimagepicker.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.rzandroid.easyimagepicker.EasyImagePicker;
import com.rzandroid.easyimagepicker.ImagePickerActivity;
import com.rzandroid.easyimagepicker.R;
import com.rzandroid.easyimagepicker.util.IntentUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GalleryProvider extends BaseProvider {
    private ImagePickerActivity activity;
    private String[] mimeTypes;
    private static final int GALLERY_INTENT_REQ_CODE = 4261;

    public GalleryProvider(ImagePickerActivity argActivity) {
        super(argActivity);
        activity = argActivity;
        Intent intent = activity.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        mimeTypes = bundle.getStringArray(EasyImagePicker.EXTRA_MIME_TYPES);
    }

    public void startIntent() {
        startGalleryIntent();
    }

    private void startGalleryIntent() {
        Intent galleryIntent = IntentUtils.getGalleryIntent(activity, mimeTypes);
        this.getActivity().startActivityForResult(galleryIntent, 4261);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int argRequestCode, int argResultCode, Intent argData) {
        if (argRequestCode == GALLERY_INTENT_REQ_CODE) {
            if (argResultCode == Activity.RESULT_OK) {
                handleResult(argData);
            } else {
                setResultCancel();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleResult(Intent argData) {
        Uri uri = argData != null ? argData.getData() : null;
        if (uri != null) {
            takePersistableUriPermission(uri);
            activity.setImage(uri);
        } else {
            setError(R.string.error_failed_pick_gallery_image);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void takePersistableUriPermission(Uri argUri) {
        activity.getContentResolver().takePersistableUriPermission(argUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
