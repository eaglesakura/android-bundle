-keepattributes Signature
-keep class com.eaglesakura.android.saver.SaveType { *; }
-keepclassmembers class * {
    @com.eaglesakura.android.saver.BundleState *;
}
