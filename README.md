# rz-android-java-sdk
Rz Android Java SDK


### Add Maven Repositories

Add maven repositories in application level

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

### Add Android Dependencies

```android_dependencies
dependencies {
    implementation "com.rzandroid.java:rzandroid-core:0.0.01"
}
```
