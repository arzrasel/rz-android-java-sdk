package com.rzandroid.device;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class ScreenResolution {
    public static float dpToPx(float argDp, Context argContext) {
        return argDp * ((float) argContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float pxToDp(float argPx, Context argContext) {
        return argPx / ((float) argContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private static Map<String, Integer> getScreenResolution(Context argContext) {
        WindowManager windowManager = (WindowManager) argContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = Integer.valueOf(metrics.widthPixels);
        int height = Integer.valueOf(metrics.heightPixels);
        int density = Integer.valueOf(metrics.densityDpi);
        //return "{" + width + "," + height + "}";
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("width", width);
        map.put("height", height);
        map.put("density", density);
        return map;
    }

}
