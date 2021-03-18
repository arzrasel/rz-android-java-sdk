# rz-android-java-sdk
Rz Android Java SDK

[![Rz Rasel](https://raw.githubusercontent.com/arzrasel/svg/main/rz-rasel-blue.svg)](https://github.com/rzrasel)
[![Download](https://api.bintray.com/packages/rzrasel/rz-android-java-sdk/rz-android-java-sdk/images/download.svg)](https://bintray.com/rzrasel/rz-android-java-sdk/rz-android-java-sdk/_latestVersion)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

### Add Android Dependencies

```android_dependencies
dependencies {
    implementation "com.rzandroid.java:rzandroid-core:0.0.04"
}
```

### Add Maven Repositories

Add maven repositories in application level (if any problem to implementation then add this code in your gradle file)

```mavenRepositoriesAppProject
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/rzrasel/rz-android-java-sdk/" }
    }
}
```

Add maven repositories in project level

```mavenRepositoriesAppProject
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/rzrasel/rz-android-java-sdk/" }
    }
}
```
