package com.rzandroid.appusagesexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.rzandroid.clicklistener.DoubleClickListener;
import com.rzandroid.clicklistener.GestureListener;
import com.rzandroid.propermission.ProPermission;

public class MainActivity extends AppCompatActivity {
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private void useProPermission() {
        ProPermission proPermission;
    }
}