# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.codecanyon.lockscreen.** {
 *;
}
-keep class com.codecanyon.lockscreen.gellery_action.** {
 *;
}
-keep class com.codecanyon.lockscreen.utils.** {
 *;
}

-dontnote android.support.v4.app.NotificationCompatJellybean
-dontnote com.google.android.gms.internal.zzry

-keep class android.support.** { *; }

-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}
-keepattributes Exceptions, InnerClasses, Signature, Deprecated,  SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.apache.http.**
-dontnote cn.finalteam.galleryfinal.**
-dontnote com.firebase.**
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.internal.**
-dontwarn org.apache.commons.logging.**