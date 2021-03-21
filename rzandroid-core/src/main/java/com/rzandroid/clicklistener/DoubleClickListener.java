package com.rzandroid.clicklistener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class DoubleClickListener extends GestureDetector.SimpleOnGestureListener {
    //
    private GestureDetector gestureDetector;
    //    private OnInternalClickListener onInternalClickListener;
    //
    @Override
    public boolean onDown(MotionEvent event) {
        //        System.out.println("DEBUG_LOG_PRINT: onDown");
        return true;
    }
    //
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //        System.out.println("DEBUG_LOG_PRINT: onSingleTapUp");
        return true;
    }
    //
    @Override
    public void onLongPress(MotionEvent event) {
        //        super.onLongPress(event);
        //        System.out.println("DEBUG_LOG_PRINT: onSingleTapUp");
    }
    //
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //        return super.onSingleTapConfirmed(event);
        //        System.out.println("DEBUG_LOG_PRINT: onSingleTapConfirmed");
        onSingleClick(event);
        return true;
    }
    //
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        //        return super.onDoubleTap(event);
        //        System.out.println("DEBUG_LOG_PRINT: onDoubleTap");
        onDoubleClick(event);
        return true;
    }
    //
    public void setListener(Context context, View view) {
        gestureDetector = new GestureDetector(context, new DoubleClickListener() {
            @Override
            public void onSingleClick(MotionEvent event) {
                onSingleClick(event);
            }
            //
            @Override
            public void onDoubleClick(MotionEvent event) {
                onDoubleClick(event);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
    // Abstract function for implementation
    abstract void onSingleClick(MotionEvent event);
    abstract void onDoubleClick(MotionEvent event);
}
/*
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.rzandroid.clicklistener.DoubleClickListener;
//
private GestureDetector gestureDetector;
private ImageView imageView;
gestureDetector = new GestureDetector(this, new DoubleClickListener() {
    @Override
    public void onSingleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDoubleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }
});
imageView.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
});
// Or
new DoubleClickListener() {
    @Override
    public void onSingleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDoubleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }
}.setListener(this, imageView);
*/
//https://www.journaldev.com/28900/android-gesture-detectors