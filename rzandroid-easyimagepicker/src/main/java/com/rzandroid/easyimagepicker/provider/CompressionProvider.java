package com.rzandroid.easyimagepicker.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.rzandroid.easyimagepicker.EasyImagePicker;
import com.rzandroid.easyimagepicker.ImagePickerActivity;
import com.rzandroid.easyimagepicker.R;
import com.rzandroid.easyimagepicker.util.ExifDataCopier;
import com.rzandroid.easyimagepicker.util.FileUtil;
import com.rzandroid.easyimagepicker.util.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import kotlin.Pair;

public class CompressionProvider extends BaseProvider {
    private ImagePickerActivity activity;
    private int maxWidth;
    private int maxHeight;
    private long maxFileSize;
    private File fileDir;
    private File compressedFile = null;
    private String THREAD_KEY = "THREAD_MESSAGE";
    private static final String TAG = CompressionProvider.class.getSimpleName();

    public CompressionProvider(ImagePickerActivity argActivity) {
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
        // Get Maximum Allowed file size
        maxFileSize = bundle.getLong(EasyImagePicker.EXTRA_IMAGE_MAX_SIZE, 0L);
        // Get File Directory
        String filePath = bundle.getString(EasyImagePicker.EXTRA_SAVE_DIRECTORY);
        fileDir = getFileDir(filePath);
    }

    private boolean isCompressEnabled() {
        return maxFileSize > 0L;
    }

    private boolean isCompressionRequired(File argFile) {
        boolean status = isCompressEnabled() && getSizeDiff(argFile) > 0L;
        if (!status && maxWidth > 0 && maxHeight > 0) {
            // Check image resolution
            Pair resolution = FileUtil.getImageResolution(argFile);
            return ((Number) resolution.getFirst()).intValue() > maxWidth || ((Number) resolution.getSecond()).intValue() > maxHeight;
        }
        return status;
    }

    public boolean isCompressionRequired(Uri argUri) throws FileNotFoundException {
        boolean status = this.isCompressEnabled() && getSizeDiff(new File(argUri.getPath())) > 0L;
        if (!status && maxWidth > 0 && maxHeight > 0) {
            Pair resolution = FileUtil.getImageResolution(this, argUri);
            return ((Number) resolution.getFirst()).intValue() > maxWidth || ((Number) resolution.getSecond()).intValue() > maxHeight;
        } else {
            return status;
        }
    }

    private long getSizeDiff(File argFile) {
        return argFile.length() - maxFileSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private long getSizeDiff(Uri uri) {
        long length = FileUtil.getImageSize((Context) this, uri);
        return length - maxFileSize;
    }

    public void compress(Uri argUri) {
        startCompressionWorker(this, argUri);
    }

    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message argMessage) {
            Bundle bundle = argMessage.getData();
            boolean value = bundle.getBoolean(THREAD_KEY);
            if (value) {
                if (compressedFile != null) {
                    // Post Result
                    handleResult(compressedFile);
                } else {
                    // Post Error
                    setError(R.string.error_failed_to_compress_image);
                }
            }
            return false;
        }
    });

    private void startCompressionWorker(Context context, Uri argUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = FileUtil.getTempFile(context, argUri);
                Message message = new Message();
                Bundle bundle = new Bundle();
                try {
                    compressedFile = startCompression(file);
                    bundle.putBoolean(THREAD_KEY, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    bundle.putBoolean(THREAD_KEY, false);
                }
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }

    private File startCompression(File argFile) throws IOException {
        File newFile = null;
        int attempt = 0;
        int lastAttempt = 0;
        do {
            if (newFile != null) {
                newFile.delete();
            }

            newFile = applyCompression(argFile, attempt);
            if (newFile == null) {
                return attempt > 0 ? applyCompression(argFile, lastAttempt) : null;
            }
            lastAttempt = attempt;
            if (maxFileSize > 0L) {
                long diff = getSizeDiff(newFile);
                attempt += diff > (long) 1024 * 1024 ? 3 : (diff > (long) 500 * 1024 ? 2 : 1);
            } else {
                ++attempt;
            }
        } while (isCompressionRequired(newFile));

        // Copy Exif Data
        ExifDataCopier.copyExif(argFile, newFile);
        return newFile;
    }

    private File applyCompression(File argFile, int argAttempt) throws IOException {
        List resList = resolutionList();
        if (argAttempt >= resList.size()) {
            return null;
        }
        int[] resolution = (int[]) resList.get(argAttempt);
        int newWidth = resolution[0];
        int newHeight = resolution[1];
        if (maxWidth > 0 && maxHeight > 0) {
            if (newWidth > maxWidth || newHeight > maxHeight) {
                newWidth = maxWidth;
                newHeight = maxHeight;
            }
        }
        // Check file format
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        if (argFile.getAbsolutePath().endsWith(".png")) {
            format = Bitmap.CompressFormat.PNG;
        }
        //
        String extension = FileUtil.getImageExtension(argFile);
        File compressFile = FileUtil.getImageFile(fileDir, extension);
        if (compressFile != null) {
            float floatWidth = (float) maxWidth;
            float floatHeight = (float) maxHeight;
            String absolutePath = compressFile.getAbsolutePath();
            return ImageUtil.compressImage(compressFile, floatWidth, floatHeight, format, absolutePath);
        }
        return null;
    }

    private List resolutionList() {
        /*List<int[]> resList = new ArrayList<>();
        resList.add(new int[]{2448, 3264});
        resList.add(new int[]{2448, 3264});*/
        return Arrays.asList(new int[][]{
                {2448, 3264},
                {2008, 3032},
                {1944, 2580},
                {1680, 2240},
                {1536, 2048},
                {1200, 1600},
                {1024, 1392},
                {960, 1280},
                {768, 1024},
                {600, 800},
                {480, 640},
                {240, 320},
                {120, 160},
                {60, 80},
                {30, 40}});
        //return Arrays.asList(new int[][]{{2448, 3264}, {2008, 3032}, {1944, 2580}, {1680, 2240}, {1536, 2048}, {1200, 1600}, {1024, 1392}, {960, 1280}, {768, 1024}, {600, 800}, {480, 640}, {240, 320}, {120, 160}, {60, 80}, {30, 40}});
        //return resList;
    }

    private void handleResult(File argFile) {
        activity.setCompressedImage(Uri.fromFile(argFile));
    }
}
