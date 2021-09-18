# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

-dontwarn com.google.**

-keepnames class com.postpc.tenq.models.**
-keep class com.postpc.tenq.models.** { *; }
-keepnames class com.postpc.tenq.viewmodels.** { *; }
-keep class com.google.android.gms.** { *; }

-dontwarn com.google.android.gms.**

-keep class com.postpc.tenq.ui.helpers.** { *; }

-keep class com.spotify.protocol.mappers.** { *; }
-keepnames class com.spotify.protocol.mappers.**
-keep class com.google.firebase.database.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keep class com.fasterxml.jackson.** { *; }
-keepnames class org.bouncycastle.jsse.** { *; }
-keep class org.bouncycastle.jsse.** { *; }
-keepnames class org.conscrypt.** { *; }
-keep class org.conscrypt.** { *; }
-keepnames class org.openjsse.javax.net.ssl.** { *; }
-keep class org.openjsse.javax.net.ssl.** { *; }
-keepnames class org.openjsse.net.ssl.** { *; }
-keep class org.openjsse.net.ssl.** { *; }

-keepclassmembers enum * { *; }

-keepattributes *Annotation*,Signature
-keepattributes SourceFile,LineNumberTable

# Required
-keepattributes Signature,InnerClasses,Annotation
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn sun.misc.Unsafe
-dontwarn java.lang.ClassValue
-keep class com.google.j2objc.annotations.** { *; }
-keep class java.lang.ClassValue { *; }
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
