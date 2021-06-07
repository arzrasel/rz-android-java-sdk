package com.rzandroid.easyimagepicker.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kotlin.io.ByteStreamsKt;
import kotlin.jvm.internal.Intrinsics;

public class FileUriUtils {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPath(Context argContext, Uri argUri) {
        String path = getPathFromLocalUri(argContext, argUri);
        if (path == null) {
            path = getPathFromRemoteUri(argContext, argUri);
        }
        return path;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromLocalUri(Context argContext, Uri argUri) {
        boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        if (isKitKat && DocumentsContract.isDocumentUri(argContext, argUri)) {
            if (isExternalStorageDocument(argUri)) {
                String docId = DocumentsContract.getDocumentId(argUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        //Environment.getExternalStorageDirectory().toString() + "/" + split[1];
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1];
                    } else {
                        //Environment.getExternalStorageDirectory().toString() + "/";
                        return Environment.getExternalStorageDirectory().toString() + "/";
                    }
                } else {
                    String path = "storage" + "/" + docId.replace(":", "/");
                    if (new File(path).exists()) {
                        return path;
                    } else {
                        return "/storage/sdcard/" + split[1];
                    }
                }
            } else if (isDownloadsDocument(argUri)) {
                return getDownloadDocument(argContext, argUri);
            } else if (isMediaDocument(argUri)) {
                return getMediaDocument(argContext, argUri);
            }
        } else if ("content".equalsIgnoreCase(argUri.getScheme())) {
            if (isGooglePhotosUri(argUri)) {
                return argUri.getLastPathSegment();
            } else {
                return getDataColumn(argContext, argUri, null, null);
            }
        } else if ("file".equalsIgnoreCase(argUri.getScheme())) {
            return argUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context argContext, Uri argUri, String argSelection, String[] argSelectionArgs) {
        String filePath;
        String column = "_data";
        String[] projection = new String[]{column};
        //
        Cursor cursor = null;
        ContentResolver contentResolver = argContext.getContentResolver();
        /*cursor = contentResolver.query(argUri, projection, argSelection, argSelectionArgs, null);
        if (cursor == null) {
            filePath = argUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;*/
        try {
            cursor = contentResolver.query(argUri, projection, argSelection, argSelectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getDownloadDocument(Context argContext, Uri argUri) {
        String fileName = getFilePath(argContext, argUri);
        if (fileName != null) {
            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
            if ((new File(path)).exists()) {
                return path;
            }
        }
        String id = DocumentsContract.getDocumentId(argUri);
        if (id.contains(":")) {
            id = id.split(":")[1];
        }
        Uri contentUri = Uri.parse("content://downloads/public_downloads");
        Long longId = Long.valueOf(id);
        contentUri = ContentUris.withAppendedId(contentUri, longId);
        return getDataColumn(argContext, contentUri, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getMediaDocument(Context argContext, Uri argUri) {
        String docId = DocumentsContract.getDocumentId(argUri);
        String[] split = docId.split(":");
        String type = split[0];
        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String selection = "_id=?";
        String[] selectionArgs = new String[]{split[1]};
        return getDataColumn(argContext, contentUri, selection, selectionArgs);
    }

    private static String getPathFromRemoteUri(Context argContext, Uri argUri) {
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        boolean success = false;
        //
        try {
            ContentResolver contentResolver = argContext.getContentResolver();
            String extension = FileUtil.getImageExtension(argUri);
            inputStream = contentResolver.openInputStream(argUri);
            file = FileUtil.getImageFile(argContext.getCacheDir(), extension);
            if (file == null) {
                return null;
            }
            outputStream = (OutputStream) (new FileOutputStream(file));
            if (inputStream != null) {
                //ByteStreamsKt.copyTo(inputStream, outputStream, 4096);
                ByteStreamsKt.copyTo(inputStream, outputStream, 4 * 1024);
                success = true;
            }
        } catch (IOException ex) {
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
            }

            try {
                outputStream.close();
            } catch (IOException ex) {
                // If closing the output stream fails, we cannot be sure that the
                // target file was written in full. Flushing the stream merely moves
                // the bytes into the OS, not necessarily to the file.
                success = false;
            }
        }
        if (success) {
            return file.getPath();
        }
        return null;
    }

    private static String getFilePath(Context argContext, Uri argUri) {
        Cursor cursor = null;
        String[] projection = new String[]{"_display_name"};
        ContentResolver contentResolver = argContext.getContentResolver();
        try {
            cursor = contentResolver.query(argUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri argUri) {
        return Intrinsics.areEqual("com.android.externalstorage.documents", argUri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri argUri) {
        return Intrinsics.areEqual("com.android.providers.downloads.documents", argUri.getAuthority());
    }

    public static boolean isMediaDocument(Uri argUri) {
        return Intrinsics.areEqual("com.android.providers.media.documents", argUri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri argUri) {
        return Intrinsics.areEqual("com.google.android.apps.photos.content", argUri.getAuthority());
    }
}
