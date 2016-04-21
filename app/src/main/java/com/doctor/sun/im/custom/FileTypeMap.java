package com.doctor.sun.im.custom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.doctor.sun.R;

import java.io.File;
import java.util.HashMap;

/**
 * Created by rick on 20/4/2016.
 */
public class FileTypeMap {
    private static final HashMap<String, Integer> fileIcon = new HashMap<>();
    private static final HashMap<String, String> mimeType = new HashMap<>();

    static {
        fileIcon.put("excel", R.drawable.file_ic_detail_excel);
        fileIcon.put("gif", R.drawable.file_ic_detail_gif);
        fileIcon.put("html", R.drawable.file_ic_detail_html);
        fileIcon.put("jpg", R.drawable.file_ic_detail_jpg);
        fileIcon.put("mp3", R.drawable.file_ic_detail_mp3);
        fileIcon.put("mp4", R.drawable.file_ic_detail_mp4);
        fileIcon.put("pdf", R.drawable.file_ic_detail_pdf);
        fileIcon.put("png", R.drawable.file_ic_detail_png);
        fileIcon.put("ppt", R.drawable.file_ic_detail_ppt);
        fileIcon.put("rar", R.drawable.file_ic_detail_rar);
        fileIcon.put("txt", R.drawable.file_ic_detail_txt);
        fileIcon.put("word", R.drawable.file_ic_detail_word);
        fileIcon.put("zip", R.drawable.file_ic_detail_zip);
    }

    static {
        mimeType.put("3gp", "video/3gpp");
        mimeType.put("apk", "application/vnd.android.package-archive");
        mimeType.put("asf", "video/x-ms-asf");
        mimeType.put("avi", "video/x-msvideo");
        mimeType.put("bin", "application/octet-stream");
        mimeType.put("bmp", "image/bmp");
        mimeType.put("c", "text/plain");
        mimeType.put("class", "application/octet-stream");
        mimeType.put("conf", "text/plain");
        mimeType.put("cpp", "text/plain");
        mimeType.put("doc", "application/msword");
        mimeType.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeType.put("xls", "application/vnd.ms-excel");
        mimeType.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeType.put("exe", "application/octet-stream");
        mimeType.put("gif", "image/gif");
        mimeType.put("gtar", "application/x-gtar");
        mimeType.put("gz", "application/x-gzip");
        mimeType.put("h", "text/plain");
        mimeType.put("htm", "text/html");
        mimeType.put("html", "text/html");
        mimeType.put("jar", "application/java-archive");
        mimeType.put("java", "text/plain");
        mimeType.put("jpeg", "image/jpeg");
        mimeType.put("jpg", "image/jpeg");
        mimeType.put("js", "application/x-javascript");
        mimeType.put("log", "text/plain");
        mimeType.put("m3u", "audio/x-mpegurl");
        mimeType.put("m4a", "audio/mp4a-latm");
        mimeType.put("m4b", "audio/mp4a-latm");
        mimeType.put("m4p", "audio/mp4a-latm");
        mimeType.put("m4u", "video/vnd.mpegurl");
        mimeType.put("m4v", "video/x-m4v");
        mimeType.put("mov", "video/quicktime");
        mimeType.put("mp2", "audio/x-mpeg");
        mimeType.put("mp3", "audio/x-mpeg");
        mimeType.put("mp4", "video/mp4");
        mimeType.put("mpc", "application/vnd.mpohun.certificate");
        mimeType.put("mpe", "video/mpeg");
        mimeType.put("mpeg", "video/mpeg");
        mimeType.put("mpg", "video/mpeg");
        mimeType.put("mpg4", "video/mp4");
        mimeType.put("mpga", "audio/mpeg");
        mimeType.put("msg", "application/vnd.ms-outlook");
        mimeType.put("ogg", "audio/ogg");
        mimeType.put("pdf", "application/pdf");
        mimeType.put("png", "image/png");
        mimeType.put("pps", "application/vnd.ms-powerpoint");
        mimeType.put("ppt", "application/vnd.ms-powerpoint");
        mimeType.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        mimeType.put("prop", "text/plain");
        mimeType.put("rc", "text/plain");
        mimeType.put("rmvb", "audio/x-pn-realaudio");
        mimeType.put("rtf", "application/rtf");
        mimeType.put("sh", "text/plain");
        mimeType.put("tar", "application/x-tar");
        mimeType.put("tgz", "application/x-compressed");
        mimeType.put("txt", "text/plain");
        mimeType.put("wav", "audio/x-wav");
        mimeType.put("wma", "audio/x-ms-wma");
        mimeType.put("wmv", "audio/x-ms-wmv");
        mimeType.put("wps", "application/vnd.ms-works");
        mimeType.put("xml", "text/plain");
        mimeType.put("z", "application/x-compress");
        mimeType.put("zip", "application/x-zip-compressed");
        mimeType.put("", "*/*");
    }

    public static int getDrawable(String key) {
        Integer integer = fileIcon.get(key);
        if (integer == null) {
            return R.drawable.file_ic_detail_unknow;
        }
        return integer;
    }

    public static void openFile(Activity activity, File file, String extension) {
        Intent intent = intentFor(file, extension);
        activity.startActivity(intent);
    }

    @NonNull
    private static Intent intentFor(File file, String extension) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, mimeType.get(extension));
        return intent;
    }
}
