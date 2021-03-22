# rz-android-java-sdk
Rz Android Java SDK

[![Rz Rasel](https://raw.githubusercontent.com/arzrasel/svg/main/rz-rasel-blue.svg)](https://github.com/rzrasel)
[![Download](https://api.bintray.com/packages/rzrasel/rz-android-java-sdk/rz-android-java-sdk/images/download.svg)](https://bintray.com/rzrasel/rz-android-java-sdk/rz-android-java-sdk/_latestVersion)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

### Add Android Dependencies

```android_dependencies
dependencies {
    implementation "com.rzandroid.java:rzandroid-core:0.0.07"
}
```

### Add Maven Repositories (If any problem to implementation)

Add maven repositories in application level (if any problem to implementation then add this code in your gradle file)

```mavenRepositoriesAppProject
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/rzrasel/rz-android-java-sdk" }
    }
}
```

Or add maven repositories in project level

```mavenRepositoriesAppProject
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/rzrasel/rz-android-java-sdk" }
    }
}
```

### Use of SQLite database class

### Use of Double Click Listener

First option:

Variable declaration

```implementationDoubleClickListener01
private GestureDetector gestureDetector;
private Button button;
```
Code implementation
```implementationDoubleClickListener02
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
button.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
});
```

Second option:

Variable declaration

```implementationDoubleClickListener03
private Button button;
```
Code implementation
```implementationDoubleClickListener04
DoubleClickListener.setListener(this, button, new DoubleClickListener.OnClickListener() {
    @Override
    public void onSingleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDoubleClick(MotionEvent event) {
        // TODO Auto-generated method stub
    }
});
```
Use in kotlin
```implementationDoubleClickListener04
private Button button;
```
Code implementation
```implementationDoubleClickListener05
DoubleClickListener.setListener(context, itemView, object: DoubleClickListener.OnClickListener, GestureListener.OnClickListener {
    override fun onSingleClick(event: MotionEvent?) {
        // TODO Auto-generated method stub
    }
    //
    override fun onDoubleClick(event: MotionEvent?) {
        // TODO Auto-generated method stub
    }
})
```
