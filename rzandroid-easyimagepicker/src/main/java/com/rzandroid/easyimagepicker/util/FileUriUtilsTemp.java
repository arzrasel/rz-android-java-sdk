package com.rzandroid.easyimagepicker.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUriUtilsTemp {
    public static String getRealPath(Context argContext, Uri argContentUri) {
        return getRealPathFromUri(argContext, argContentUri);
    }

    public static String getRealPathFromUri(Context argContext, Uri argContentUri) {
        String filePath;
        Cursor cursor = argContext.getContentResolver().query(argContentUri, null, null, null, null);
        if (cursor == null) {
            filePath = argContentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }
}
