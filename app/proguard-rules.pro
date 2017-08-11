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
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**
-keep public class * extends android.app.Fragment
-keep class * extends java.lang.annotation.Annotation { *; }#注解包下的所有内容不要混淆
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
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
# 同上
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# app
-keep class com.doctor.sun.entity.** {*;}
-keep class com.doctor.auto.**{*;}
-keep class com.doctor.sun.bean.** {*;}
-keep class com.doctor.sun.dto.** {*;}
-keep class com.doctor.sun.immutables.** {*;}
-keep class com.doctor.sun.model.** {*;}
-keep class com.doctor.sun.util.** {*;}
-keep class com.doctor.sun.ui.activity.patient.POrderMessageActivity
-keep class com.doctor.sun.ui.fragment.BaseFragmentFactory {*;}
-keep class com.doctor.sun.util.FragmentFactory {*;}
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
-keep class io.ganguo.app.cache.** { *; }
-keep class com.google.common.base.Strings** { *; }
-keep class com.google.common.collect.Collections2** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.**{*;}
-keep class com.alibaba.fastjson.**{*;}
-keep class org.jsoup.** { *; }
-keep class com.alimama.moblie.** { *; }
-keep class u.upd.** { *; }
-keep class cn.sharesdk.** { *; }
-keep class com.mop.** { *; }
-keep class com.loopj.android.http.** { *; }
-keep class com.nostra13.universalimageloader.** { *; }
-keep class com.squareup.** { *; }
-keep class com.umeng.** { *; }
-keepclassmembers class **{*;}
-keep class com.afollestad.** { *; }
-keep class android.os.** { *; }
-keep class com.sina.weibo.** { *; }

-dontwarn org.apache.**
-dontwarn io.ganguo.app.cache.**
-dontwarn com.google.**
-dontwarn com.alibaba.fastjson.**
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
#-keep public class JacksonUtils.** {
#  public void set*(***);
#  public *** get*();
#}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


# Glide specific rules #
# https://github.com/bumptech/glide

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontwarn java.beans.**
-dontwarn com.taobao.**
-dontwarn com.apache.http.**
-dontwarn android.net.**
-dontwarn org.w3c.**

-dontwarn android.os.Messenger

-dontwarn com.netease.**
-dontwarn io.netty.**
-keep class com.netease.** {*;}
#如果 netty 使用的官方版本，它中间用到了反射，因此需要 keep。如果使用的是我们提供的版本，则不需要 keep
-keep class io.netty.** {*;}

#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}