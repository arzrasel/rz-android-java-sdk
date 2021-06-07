package com.rzandroid.easyimagepicker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class FileUtil {
    public static File getImageFile(File argFileDir, String argExtension) {
        try {
            // Create an image file name
            String ext = argExtension;
            if (argExtension == null) {
                ext = ".jpg";
            }
            String fileName = getFileName();
            String imageFileName = fileName + ext;
            if (!argFileDir.exists()) {
                argFileDir.mkdirs();
            }

            File file = new File(argFileDir, imageFileName);
            file.createNewFile();
            return file;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getFileName() {
        return "IMG_" + getTimestamp();
    }

    private static String getTimestamp() {
        String timeFormat = "yyyyMMdd_HHmmssSSS";
        String timeStamp = (new SimpleDateFormat(timeFormat, Locale.getDefault())).format(new Date());
        return timeStamp;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public long getFreeSpace(File argFile) {
        StatFs stat = new StatFs(argFile.getPath());
        long availBlocks = stat.getAvailableBlocksLong();
        long blockSize = stat.getBlockSizeLong();
        return availBlocks * blockSize;
    }

    public static Pair getImageResolution(Context argContext, Uri argUri) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream stream = argContext.getContentResolver().openInputStream(argUri);
        BitmapFactory.decodeStream(stream, (Rect) null, options);
        return new Pair(options.outWidth, options.outHeight);
    }

    public static Pair getImageResolution(File argFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(argFile.getAbsolutePath(), options);
        return new Pair(options.outWidth, options.outHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static long getImageSize(Context argContext, Uri argUri) {
        DocumentFile documentFile = getDocumentFile(argContext, argUri);
        return documentFile != null ? documentFile.length() : 0L;
    }

    public static File getTempFile(Context argContext, Uri argUri) {

        try {
            File destination = new File(argContext.getCacheDir(), "image_picker.png");
            ParcelFileDescriptor parcelFileDescriptor = argContext.getContentResolver().openFileDescriptor(argUri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                if (fileDescriptor != null) {
                    FileChannel src = (new FileInputStream(fileDescriptor)).getChannel();
                    FileChannel dst = (new FileOutputStream(destination)).getChannel();
                    dst.transferFrom((ReadableByteChannel) src, 0L, src.size());
                    src.close();
                    dst.close();
                    return destination;
                }
            }

            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static DocumentFile getDocumentFile(Context argContext, Uri argUri) {
        DocumentFile file = (DocumentFile) null;
        if (isFileUri(argUri)) {
            String path = FileUriUtils.getRealPath(argContext, argUri);
            if (path != null) {
                file = DocumentFile.fromFile(new File(path));
            }
        } else {
            file = DocumentFile.fromSingleUri(argContext, argUri);
        }

        return file;
    }

    public static Bitmap.CompressFormat getCompressFormat(String argExtension) {
        return StringsKt.contains((CharSequence) argExtension, (CharSequence) "png", true) ? Bitmap.CompressFormat.PNG : (StringsKt.contains((CharSequence) argExtension, (CharSequence) "webp", true) ? (Build.VERSION.SDK_INT >= 30 ? Bitmap.CompressFormat.WEBP_LOSSLESS : Bitmap.CompressFormat.WEBP) : Bitmap.CompressFormat.JPEG);
    }

    public static String getImageExtension(File argFile) {
        Uri uri = Uri.fromFile(argFile);
        return getImageExtension(uri);
    }

    public static String getImageExtension(Uri argUriImage) {
        String extension = null;
        try {
            String imagePath = argUriImage.getPath();
            if (imagePath != null && imagePath.lastIndexOf(".") != -1) {
                extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            }
        } catch (Exception ex) {
            extension = null;
        }
        return extension;
    }

    private static boolean isFileUri(Uri argUri) {
        return StringsKt.equals("file", argUri.getScheme(), true);
    }
}
