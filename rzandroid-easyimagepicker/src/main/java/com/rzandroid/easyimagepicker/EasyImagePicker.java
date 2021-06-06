package com.rzandroid.easyimagepicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

public class EasyImagePicker {
    private Activity activity;
    private Fragment fragment;
    public static int REQUEST_CODE = 2404;
    public static int RESULT_ERROR = 64;
    private String EXTRA_IMAGE_PROVIDER = "extra.image_provider";
    private String EXTRA_CAMERA_DEVICE = "extra.camera_device";
    private String EXTRA_IMAGE_MAX_SIZE = "extra.image_max_size";
    private String EXTRA_CROP = "extra.crop";
    private String EXTRA_CROP_X = "extra.crop_x";
    private String EXTRA_CROP_Y = "extra.crop_y";
    private String EXTRA_MAX_WIDTH = "extra.max_width";
    private String EXTRA_MAX_HEIGHT = "extra.max_height";
    private String EXTRA_SAVE_DIRECTORY = "extra.save_directory";
    private String EXTRA_ERROR = "extra.error";
    private String EXTRA_FILE_PATH = "extra.file_path";
    private String EXTRA_MIME_TYPES = "extra.mime_types";

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
        private Fragment fragment = null;
        public Builder(Activity argActivity) {
            //
        }
        public Builder(Fragment argFragment) {
            //
        }
    }
}
