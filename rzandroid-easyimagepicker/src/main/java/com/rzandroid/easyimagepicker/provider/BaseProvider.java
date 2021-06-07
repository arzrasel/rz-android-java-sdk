package com.rzandroid.easyimagepicker.provider;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;

import com.rzandroid.easyimagepicker.ImagePickerActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class BaseProvider extends ContextWrapper {
    private ImagePickerActivity activity;

    public BaseProvider(ImagePickerActivity argActivity) {
        super(argActivity);
        activity = argActivity;
    }

    public File getFileDir(String argPath) {
        File file;
        if (argPath != null) {
            file = new File(argPath);
        } else {
            file = getExternalFilesDir(Environment.DIRECTORY_DCIM);
            if (file == null) {
                file = this.activity.getFilesDir();
            }
        }

        return file;
    }

    protected void setError(String argError) {
        onFailure();
        activity.setError(argError);
    }

    protected void setError(int argErrorRes) {
        String error = getString(argErrorRes);
        setError(error);
    }

    protected void setResultCancel() {
        onFailure();
        activity.setResultCancel();
    }

    protected void onFailure() {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    protected ImagePickerActivity getActivity() {
        return activity;
    }
}
