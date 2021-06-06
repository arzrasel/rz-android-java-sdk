package com.rzandroid.easyimagepicker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import kotlin.jvm.internal.Intrinsics;

public class ImageUtil {
    public File compressImage(File argImageFile, float argReqWidth, float argReqHeight, Bitmap.CompressFormat argCompressFormat, String argDestinationPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        File file = new File(argDestinationPath).getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        //
        try {
            fileOutputStream = new FileOutputStream(argDestinationPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            /*Bitmap bitmap = decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight) ?.compress(
                    compressFormat,
                    100,
                    fileOutputStream
            )*/
            Bitmap bitmap = decodeSampledBitmapFromFile(argImageFile, argReqWidth, argReqHeight);
            bitmap.compress(argCompressFormat, 100, (OutputStream) fileOutputStream);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        //
        return new File(argDestinationPath);
    }

    private Bitmap decodeSampledBitmapFromFile(File argImageFile, float argReqWidth, float argReqHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(argImageFile.getAbsolutePath(), options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = argReqWidth / argReqHeight;
        if ((float) actualHeight > argReqHeight || (float) actualWidth > argReqWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = argReqHeight / (float) actualHeight;
                actualWidth = (int) (imgRatio * (float) actualWidth);
                actualHeight = (int) argReqHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = argReqWidth / (float) actualWidth;
                actualHeight = (int) (imgRatio * (float) actualHeight);
                actualWidth = (int) argReqWidth;
            } else {
                actualHeight = (int) argReqHeight;
                actualWidth = (int) argReqWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        if (bitmap != null && canUseForInBitmap(bitmap, options)) {
            options.inMutable = true;
            options.inBitmap = bitmap;
        }
        options.inTempStorage = new byte[16384];
        try {
            bitmap = BitmapFactory.decodeFile(argImageFile.getAbsolutePath(), options);
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }
        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }
        float ratioX = (float) actualWidth / (float) options.outWidth;
        float ratioY = (float) actualHeight / (float) options.outHeight;
        float middleX = (float) actualWidth / 2.0F;
        float middleY = (float) actualHeight / 2.0F;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Intrinsics.checkNotNull(scaledBitmap);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Intrinsics.checkNotNull(bitmap);
        canvas.drawBitmap(bitmap, middleX - (float) (bitmap.getWidth() / 2), middleY - (float) (bitmap.getHeight() / 2), new Paint(2));
        bitmap.recycle();
        Matrix matrix = new Matrix();
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return scaledBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options argOptions, int argReqWidth, int argReqHeight) {
        int height = argOptions.outHeight;
        int width = argOptions.outWidth;
        int inSampleSize = 1;
        if (height > argReqHeight || width > argReqWidth) {
            int halfHeight = height / 2;

            for (int halfWidth = width / 2; halfHeight / inSampleSize >= argReqHeight && halfWidth / inSampleSize >= argReqWidth; inSampleSize *= 2) {
            }
        }

        return inSampleSize;
    }

    private boolean canUseForInBitmap(Bitmap argCandidate, BitmapFactory.Options argTargetOptions) {
        boolean canUseBitmap;
        if (Build.VERSION.SDK_INT >= 19) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            int width = argTargetOptions.outWidth / argTargetOptions.inSampleSize;
            int height = argTargetOptions.outHeight / argTargetOptions.inSampleSize;
            Bitmap.Config config = argCandidate.getConfig();
            Intrinsics.checkNotNullExpressionValue(config, "candidate.config");
            int byteCount = width * height * getBytesPerPixel(config);
            canUseBitmap = byteCount <= argCandidate.getAllocationByteCount();
        } else {
            canUseBitmap = argCandidate.getWidth() == argTargetOptions.outWidth && argCandidate.getHeight() == argTargetOptions.outHeight && argTargetOptions.inSampleSize == 1;
        }

        return canUseBitmap;
    }

    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }
    /*private static <T extends Throwable> T sanitizeStackTrace(T throwable) {
        return Intrinsics.sanitizeStackTrace(throwable, TypeIntrinsics.class.getName());
    }*/
}
