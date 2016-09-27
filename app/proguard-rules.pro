# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/zhihui_chen/Data/develop/android/tools/android-studio/sdk/tools/proguard/proguard-android.txt
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
-optimizations !class/unboxing/enum
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.alipay.**
-dontwarn android.support.**
-dontwarn android.support.v4.**
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# app
-keep class com.doctor.sun.entity.** {*;}
-keep class com.doctor.sun.bean.** {*;}
-keep class com.doctor.sun.dto.** {*;}
-keep class com.doctor.sun.ui.** {*;}
-keep class com.doctor.sun.vo.** {*;}
-dontwarn com.doctor.sun.AppContext
-dontwarn io.ganguo.library.BaseContext
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.doctor.sun.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
}

# 不混淆的第三方
-keepattributes *Annotation*
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class io.ganguo.app.cache.** { *; }
-keep class com.google.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class org.jsoup.** { *; }
-keep class com.alimama.moblie.** { *; }
-keep class u.upd.** { *; }
-keep class cn.sharesdk.** { *; }
-keep class com.mop.** { *; }
-keep class com.loopj.android.http.** { *; }
-keep class com.nostra13.universalimageloader.** { *; }
-keep class com.squareup.** { *; }
-keep class com.umeng.** { *; }
-keep class com.afollestad.** { *; }
-keep class android.os.** { *; }

-dontwarn org.apache.**
-dontwarn io.ganguo.app.cache.**
-dontwarn com.google.**
-dontwarn com.google.gson.examples.android.model.**
-dontwarn org.jsoup.**
-dontwarn com.alimama.moblie.**
-dontwarn u.upd.**
-dontwarn cn.sharesdk.**
-dontwarn com.mop.**
-dontwarn com.loopj.android.http.**
-dontwarn com.nostra13.universalimageloader.**
-dontwarn com.squareup.**
-dontwarn com.umeng.**
-dontwarn com.afollestad.**

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keep class org.codehaus.** { *; }
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep public class JacksonUtils.** {
  public void set*(***);
  public *** get*();
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Glide specific rules #
# https://github.com/bumptech/glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-dontwarn okio.**
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Paths
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn java.beans.**
-dontwarn com.taobao.**
-dontwarn com.apache.http.**
-dontwarn android.net.**
-dontwarn org.w3c.**

-dontwarn android.os.Messenger
