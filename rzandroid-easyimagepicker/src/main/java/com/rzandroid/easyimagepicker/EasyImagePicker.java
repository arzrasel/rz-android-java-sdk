package com.rzandroid.easyimagepicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.rzandroid.easyimagepicker.constant.ImageProvider;
import com.rzandroid.easyimagepicker.listener.DismissListener;
import com.rzandroid.easyimagepicker.listener.ResultListener;
import com.rzandroid.easyimagepicker.util.DialogHelper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class EasyImagePicker {
    private Activity activity;
    private Fragment fragment;
    public static final int REQUEST_CODE = 2404;
    public static final int RESULT_ERROR = 64;
    public static final String EXTRA_IMAGE_PROVIDER = "extra.image_provider";
    public static final String EXTRA_CAMERA_DEVICE = "extra.camera_device";
    public static final String EXTRA_IMAGE_MAX_SIZE = "extra.image_max_size";
    public static final String EXTRA_CROP = "extra.crop";
    public static final String EXTRA_CROP_X = "extra.crop_x";
    public static final String EXTRA_CROP_Y = "extra.crop_y";
    public static final String EXTRA_MAX_WIDTH = "extra.max_width";
    public static final String EXTRA_MAX_HEIGHT = "extra.max_height";
    public static final String EXTRA_SAVE_DIRECTORY = "extra.save_directory";
    public static final String EXTRA_ERROR = "extra.error";
    public static final String EXTRA_FILE_PATH = "extra.file_path";
    public static final String EXTRA_MIME_TYPES = "extra.mime_types";

    public Builder with(Activity argActivity) {
        activity = argActivity;
        return new Builder(activity);

    }

    public Builder with(Fragment argFragment) {
        fragment = argFragment;
        return new Builder(fragment);

    }

    public String getError(Intent argIntent) {
        String error = argIntent.getStringExtra(EXTRA_ERROR);
        if (error != null) {
            return error;
        } else {
            return "Unknown Error!";
        }
    }

    public class Builder {
        private Fragment fragment;
        private ImageProvider imageProvider;
        private String[] mimeTypes;
        private float cropX;
        private float cropY;
        private boolean crop;
        private int maxWidth;
        private int maxHeight;
        private long maxSize;
        private Function1 imageProviderInterceptor;
        private DismissListener dismissListener;
        private String saveDir;
        private Activity activity;

        public Builder(Activity argActivity) {
            super();
            activity = activity;
            imageProvider = ImageProvider.BOTH;
            mimeTypes = new String[0];
        }

        public Builder(Fragment argFragment) {
            this(argFragment.getActivity());
            this.fragment = argFragment;
        }

        public Builder provider(ImageProvider argImageProvider) {
            imageProvider = argImageProvider;
            return this;
        }

        public final Builder cameraOnly() {
            imageProvider = ImageProvider.CAMERA;
            return this;
        }

        public final Builder galleryOnly() {
            imageProvider = ImageProvider.GALLERY;
            return this;
        }

        public Builder galleryMimeTypes(String[] argMimeTypes) {
            mimeTypes = argMimeTypes;
            return this;
        }

        public Builder crop(float x, float y) {
            cropX = x;
            cropY = y;
            return crop();
        }


        public Builder crop() {
            this.crop = true;
            return this;
        }

        public Builder cropSquare() {
            return crop(1.0F, 1.0F);
        }

        public Builder maxResultSize(int argWidth, int argHeight) {
            maxWidth = argWidth;
            maxHeight = argHeight;
            return this;
        }

        public Builder compress(int argMaxSize) {
            maxSize = (long) argMaxSize * 1024L;
            return this;
        }

        public Builder saveDir(String argPath) {
            saveDir = argPath;
            return this;
        }

        public Builder saveDir(File argFile) {
            saveDir = argFile.getAbsolutePath();
            return this;
        }

        /*public Builder setImageProviderInterceptor(Function1 argInterceptor) {
            imageProviderInterceptor = argInterceptor;
            return this;
        }*/

        public Builder setDismissListener(DismissListener argListener) {
            dismissListener = argListener;
            return this;
        }
        /*public  Builder setDismissListener( Function0 argListener) {
            Intrinsics.checkNotNullParameter(listener, "listener");
            this.dismissListener = (DismissListener)(new DismissListener() {
                public void onDismiss() {
                    listener.invoke();
                }
            });
            return this;
        }*/

        public void start() {
            start(REQUEST_CODE);
        }

        public void start(int argReqCode) {
            if (imageProvider == ImageProvider.BOTH) {
                showImageProviderDialog(argReqCode);
            } else {
                startActivity(argReqCode);
            }
        }

        private Intent createIntent() {
            Intent intent = new Intent(activity, ImagePickerActivity.class);
            intent.putExtras(getBundle());
            return intent;
        }

        private void showImageProviderDialog(int argReqCode) {
            new DialogHelper().showChooseAppDialog(activity, new ResultListener() {
                @Override
                public void onResult(Object argResult) {
                    imageProvider = (ImageProvider) argResult;
                    startActivity(argReqCode);
                }
            }, dismissListener);
        }

        private Bundle getBundle() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_IMAGE_PROVIDER, imageProvider);
            bundle.putStringArray(EXTRA_MIME_TYPES, mimeTypes);
            //
            bundle.putBoolean(EXTRA_CROP, crop);
            bundle.putFloat(EXTRA_CROP_X, cropX);
            bundle.putFloat(EXTRA_CROP_Y, cropY);
            //
            bundle.putInt(EXTRA_MAX_WIDTH, maxWidth);
            bundle.putInt(EXTRA_MAX_HEIGHT, maxHeight);
            //
            bundle.putLong(EXTRA_IMAGE_MAX_SIZE, maxSize);
            bundle.putString(EXTRA_SAVE_DIRECTORY, saveDir);
            return bundle;
        }

        private void startActivity(int argReqCode) {
            Intent intent = new Intent(activity, ImagePickerActivity.class);
            intent.putExtras(getBundle());
            if (fragment != null) {
                fragment.startActivityForResult(intent, argReqCode);
            } else {
                activity.startActivityForResult(intent, argReqCode);
            }
        }
    }
}
