package com.doctor.sun.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by rick on 13/4/2016.
 */
public class PickFileUtils {
    public static File getFile(Context context, Intent data) {
        File file;
        Uri uri = data.getData();
        //log: uri.getScheme() --> file / content
        switch (uri.getScheme()) {
            case "content": {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                cursor.close();
                if (picturePath != null) {
                    file = new File(picturePath);
                } else {
                    file = null;
                }
                break;
            }
            case "file": {
                file = new File(uri.getPath());
                break;
            }
            default: {
                file = null;
            }
        }
        return file;
    }
}
