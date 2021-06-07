package com.rzandroid.easyimagepicker.util;

import android.util.Log;

import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class ExifDataCopier {
    public static void copyExif(File argFilePathOri, File argFilePathDest) {
        try {
            ExifInterface oldExif = new ExifInterface(argFilePathOri);
            ExifInterface newExif = new ExifInterface(argFilePathDest);
            List attributes = CollectionsKt.listOf(new String[]{"FNumber", "ExposureTime", "ISOSpeedRatings", "GPSAltitude", "GPSAltitudeRef", "FocalLength", "GPSDateStamp", "WhiteBalance", "GPSProcessingMethod", "GPSTimeStamp", "DateTime", "Flash", "GPSLatitude", "GPSLatitudeRef", "GPSLongitude", "GPSLongitudeRef", "Make", "Model", "Orientation"});
            Iterator iterator = attributes.iterator();

            while(iterator.hasNext()) {
                String attribute = (String)iterator.next();
                setIfNotNull(oldExif, newExif, attribute);
            }

            newExif.saveAttributes();
        } catch (Exception var8) {
            Log.e("ExifDataCopier", "Error preserving Exif data on selected image: " + var8);
        }
    }
    private static void setIfNotNull(ExifInterface argOldExif, ExifInterface argNewExif, String argProperty) {
        if (argOldExif.getAttribute(argProperty) != null) {
            argNewExif.setAttribute(argProperty, argOldExif.getAttribute(argProperty));
        }

    }
}
