package com.doctor.sun.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by kb on 16-11-23.
 */

public class SaveImageUtil {

    public static String saveImage(Context context, Bitmap bmp) {
        String fileName = String.valueOf(System.currentTimeMillis());
        return MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, null);
    }
}
