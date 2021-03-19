package com.rzandroid.device;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Rz Rasel on 2016-10-08.
 */
public class DisplayDimension {
    //|----|------------------------------------------------------------|
    //|----|VARIABLE
    private Activity activity;
    private Context context;
    private String theClassName = getClass().getSimpleName();
    private String TAG_CLASS_NAME = getClass().getSimpleName();
    //|----|------------------------------------------------------------|
    private static DisplayDimension instance = null;
    //|----|------------------------------------------------------------|

    public static DisplayDimension getInstance(Context argContext) {
        if (instance == null) {
            instance = new DisplayDimension(argContext);
        }
        return instance;
    }

    private DisplayDimension() {
    }

    //|----|------------------------------------------------------------|
    public DisplayDimension(Context argContext) {
        this.context = argContext;
    }

    //|----|------------------------------------------------------------|
    public Point getDisplaySize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //Display display = getWindowManager().getDefaultDisplay();
        Display display = windowManager.getDefaultDisplay();
        /*int width = display.getWidth();
        int height = display.getHeight();*/
        Point displaySize = new Point();
        display.getSize(displaySize);
        /*int width = displaySize.x;
        int height = displaySize.y;*/
        return displaySize;
    }

    //|----|------------------------------------------------------------|
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    //|----|------------------------------------------------------------|
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    //|----|------------------------------------------------------------|
}