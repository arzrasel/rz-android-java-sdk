package com.rzandroid.easyimagepicker.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.rzandroid.easyimagepicker.R;

import java.io.File;

public class IntentUtils {
    public Intent getGalleryIntent(Context argContext, String[] argMimeTypes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = getGalleryDocumentIntent(argMimeTypes);
            if (intent.resolveActivity(argContext.getPackageManager()) != null) {
                return intent;
            }
        }
        return getLegacyGalleryPickIntent(argMimeTypes);
    }

    private Intent getGalleryDocumentIntent(String[] argMimeTypes) {
        // Show Document Intent
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).applyImageTypes(argMimeTypes);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, argMimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    private Intent getLegacyGalleryPickIntent(String[] argMimeTypes) {
        // Show Gallery Intent, Will open google photos
        Intent intent = new Intent(Intent.ACTION_PICK);
        return intent.putExtra(Intent.EXTRA_MIME_TYPES, argMimeTypes);
    }

    /*private Intent Intent.applyImageTypes(String[] argMimeTypes) {
        // Apply filter to show image only in intent
        type = "image/*"
        if (mimeTypes.isNotEmpty()) {
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        return this
    }*/
    public static Intent getCameraIntent(Context argContext, File argFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = argContext.getPackageName() + argContext.getString(R.string.image_picker_provider_authority_suffix);
            Uri photoURI = FileProvider.getUriForFile(argContext, authority, argFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(argFile));
        }

        return intent;
    }

    public static boolean isCameraAppAvailable(Context argContext) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return intent.resolveActivity(argContext.getPackageManager()) != null ? true : false;
    }
}
