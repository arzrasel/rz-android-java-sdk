package com.rzandroid.clicklistener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class GestureListener extends GestureDetector.SimpleOnGestureListener {
    //    private GestureDetector gestureDetector;
        private OnClickListenerInner onClickListenerInner;
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

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        //        return super.onDoubleTap(event);
        //        System.out.println("DEBUG_LOG_PRINT: onDoubleTap");
        onDoubleClick(event);
        return true;
    }
    //
    public static void setEventListener(Context context, View view, OnClickListener onClickListener) {
        /*GestureDetector gestureDetector = new GestureDetector(context, new GestureListener() {
            @Override
            public void onSingleClick(MotionEvent event) {
                onSingleClick(event);
            }
            //
            @Override
            public void onDoubleClick(MotionEvent event) {
                onDoubleClick(event);
            }
        });*/
        GestureDetector gestureDetector = new GestureDetector(context, new GestureListener() {
            @Override
            public void onSingleClick(MotionEvent event) {
                if(onClickListener != null) {
                    onClickListener.onSingleClick(event);
                }
            }
            //
            @Override
            public void onDoubleClick(MotionEvent event) {
                if(onClickListener != null) {
                    onClickListener.onDoubleClick(event);
                }
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }
    //
    private interface OnClickListenerInner {
        void onSingleClick(MotionEvent event);
        void onDoubleClick(MotionEvent event);
    }
    // Abstract function for implementation
    public abstract void onSingleClick(MotionEvent event);
    public abstract void onDoubleClick(MotionEvent event);
    public interface OnClickListener {
        void onSingleClick(MotionEvent event);
        void onDoubleClick(MotionEvent event);
    }
}
/*
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.rzandroid.clicklistener.GestureListener;
//
private GestureDetector gestureDetector;
private Button button;
gestureDetector = new GestureDetector(this, new GestureListener() {
    @Override
    public void onSingleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDoubleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }
});
button.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
});
// Or
GestureListener.setListener(this, button, new GestureListener.OnClickListener() {
    @Override
    public void onSingleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDoubleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }
});
*/
//https://www.journaldev.com/28900/android-gesture-detectors
