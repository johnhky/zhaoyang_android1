package com.doctor.sun.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

/**
 * Created by kb on 16-11-23.
 */

public class SaveImageUtil {

    public static void saveImage(Context context, Bitmap bmp) {
        String fileName = String.valueOf(System.currentTimeMillis());
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, null);
    }
}
