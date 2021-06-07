package com.rzandroid.easyimagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.rzandroid.easyimagepicker.constant.ImageProvider;
import com.rzandroid.easyimagepicker.provider.CameraProvider;
import com.rzandroid.easyimagepicker.provider.CompressionProvider;
import com.rzandroid.easyimagepicker.provider.CropProvider;
import com.rzandroid.easyimagepicker.provider.GalleryProvider;
import com.rzandroid.easyimagepicker.util.FileUriUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;

public class ImagePickerActivity extends AppCompatActivity {
    private GalleryProvider galleryProvider;
    private CameraProvider cameraProvider;
    private CropProvider cropProvider;
    private CompressionProvider compressionProvider;
    private static final String TAG = "image_picker";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadBundle(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle outState) {
        cameraProvider.onSaveInstanceState(outState);
        cropProvider.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    private void loadBundle(Bundle savedInstanceState) {
        // Create Crop Provider
        cropProvider = new CropProvider(this);
        cropProvider.onRestoreInstanceState(savedInstanceState);
        // Create Compression Provider
        compressionProvider = new CompressionProvider(this);
        // Retrieve Image Provider
        ImageProvider provider = (ImageProvider) getIntent().getSerializableExtra(EasyImagePicker.EXTRA_IMAGE_PROVIDER);
        // Create Gallery/Camera Provider
        if (provider != null) {
            if (provider == ImageProvider.GALLERY) {
                galleryProvider = new GalleryProvider(this);
                // Pick Gallery Image
                //galleryProvider.onSaveInstanceState(savedInstanceState);
                galleryProvider.startIntent();
            } else if (provider == ImageProvider.CAMERA) {
                cameraProvider = new CameraProvider(this);
                cameraProvider.onRestoreInstanceState(savedInstanceState);
                // Pick Camera Image
                cameraProvider.startIntent();
            }
        } else {
            // Something went Wrong! This case should never happen
            //Log.e(TAG, "Image provider can not be null");
            setError(getString(R.string.error_task_cancelled));
        }
    }

    public void onRequestPermissionsResult(int argRequestCode, String[] argPermissions, int[] argGrantResults) {
        super.onRequestPermissionsResult(argRequestCode, argPermissions, argGrantResults);
        cameraProvider.onRequestPermissionsResult(argRequestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int argRequestCode, int argResultCode, Intent argData) {
        super.onActivityResult(argRequestCode, argResultCode, argData);
        cameraProvider.onActivityResult(argRequestCode, argResultCode, argData);
        galleryProvider.onActivityResult(argRequestCode, argResultCode, argData);
        cropProvider.onActivityResult(argRequestCode, argResultCode, argData);
    }

    public void onBackPressed() {
        setResultCancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setImage(Uri argUri) throws IOException {
        if (cropProvider.isCropEnabled()) {
            cropProvider.startIntent(argUri);
        } else {
            if (compressionProvider.isCompressionRequired(argUri)) {
                compressionProvider.compress(argUri);
            } else {
                setResult(argUri);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setCropImage(Uri argUri) throws FileNotFoundException {
        // Delete Camera file after crop. Else there will be two image for the same action.
        // In case of Gallery Provider, we will get original image path, so we will not delete that.
        cameraProvider.delete();

        if (compressionProvider.isCompressionRequired(argUri)) {
            compressionProvider.compress(argUri);
        } else {
            setResult(argUri);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setCompressedImage(Uri argUri) {
        // This is the case when Crop is not enabled

        // Delete Camera file after crop. Else there will be two image for the same action.
        // In case of Gallery Provider, we will get original image path, so we will not delete that.
        cameraProvider.delete();
        // If crop file is not null, Delete it after crop
        cropProvider.delete();
        setResult(argUri);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setResult(Uri argUri) {
        Intent intent = new Intent();
        intent.setData(argUri);
        intent.putExtra(EasyImagePicker.EXTRA_FILE_PATH, FileUriUtils.getRealPath(this, argUri));
        this.setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    public void setResultCancel() {
        setResult(Activity.RESULT_CANCELED, getCancelledIntent(this));
        finish();
    }

    public final void setError(String message) {
        Intent intent = new Intent();
        intent.putExtra(EasyImagePicker.EXTRA_ERROR, message);
        setResult(EasyImagePicker.RESULT_ERROR, intent);
        finish();
    }

    public Intent getCancelledIntent(Context context) {
        Intent intent = new Intent();
        String message = context.getString(R.string.error_task_cancelled);
        intent.putExtra(EasyImagePicker.EXTRA_ERROR, message);
        return intent;
    }
}
