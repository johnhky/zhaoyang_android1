package com.doctor.sun.im.custom;

import com.doctor.sun.R;

import java.util.HashMap;

/**
 * Created by rick on 20/4/2016.
 */
public class FileTypeMap {
    private static final HashMap<String, Integer> map = new HashMap<>();

    static {
        map.put("excel", R.drawable.file_ic_detail_excel);
        map.put("gif", R.drawable.file_ic_detail_gif);
        map.put("html", R.drawable.file_ic_detail_html);
        map.put("jpg", R.drawable.file_ic_detail_jpg);
        map.put("mp3", R.drawable.file_ic_detail_mp3);
        map.put("mp4", R.drawable.file_ic_detail_mp4);
        map.put("pdf", R.drawable.file_ic_detail_pdf);
        map.put("png", R.drawable.file_ic_detail_png);
        map.put("ppt", R.drawable.file_ic_detail_ppt);
        map.put("rar", R.drawable.file_ic_detail_rar);
        map.put("txt", R.drawable.file_ic_detail_txt);
        map.put("word", R.drawable.file_ic_detail_word);
        map.put("zip", R.drawable.file_ic_detail_zip);
    }

    public static int getDrawable(String key){

        Integer integer = map.get(key);
        if (integer == null){
            return R.drawable.file_ic_detail_unknow;
        }
        return integer;
    }
}
