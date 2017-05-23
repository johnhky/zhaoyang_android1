package com.doctor.sun.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.doctor.sun.bean.Constants;

/**
 * Created by heky on 17/5/3.
 */

public class PreferenceHelper {

    public static  void wirteString(Context context,String tag,String id){
        SharedPreferences preferences = context.getSharedPreferences(Constants.MOCK,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(tag,id);
        editor.commit();
    }
    public  static String readString(Context context,String tag){
        SharedPreferences preferences = context.getSharedPreferences(Constants.MOCK,Context.MODE_PRIVATE);
        return  preferences.getString(tag,"");
    }
    public static  void remove(Context context,String tag){
        SharedPreferences preferences = context.getSharedPreferences(Constants.MOCK,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(tag);
        editor.commit();
    }
}
