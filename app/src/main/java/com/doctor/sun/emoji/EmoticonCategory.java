package com.doctor.sun.emoji;

import android.content.Context;
import android.content.res.AssetManager;

import com.doctor.sun.AppContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EmoticonCategory {

    private String name; // 贴纸包名
    private String title; // 显示的标题
    private boolean system; // 是否是系统内置表情
    private int order = 0; // 默认顺序

    private List<Emoticon> stickers;

    public EmoticonCategory(String name, String title, boolean system, int order) {
        this.title = title;
        this.name = name;
        this.system = system;
        this.order = order;

        loadStickerData();
    }

    public boolean system() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Emoticon> getStickers() {
        return stickers;
    }

    public boolean hasStickers() {
        return stickers != null && stickers.size() > 0;
    }

    public InputStream getCoverNormalInputStream(Context context) {
        String filename = name + "_s_normal.png";
        return makeFileInputStream(context, filename);
    }

    public InputStream getCoverPressedInputStream(Context context) {
        String filename = name + "_s_pressed.png";
        return makeFileInputStream(context, filename);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        if (stickers == null || stickers.isEmpty()) {
            return 0;
        }

        return stickers.size();
    }

    public int getOrder() {
        return order;
    }

    private InputStream makeFileInputStream(Context context, String filename) {
        try {
            if (system) {
                AssetManager assetManager = context.getResources().getAssets();
                String path = "sticker/" + filename;
                return assetManager.open(path);
            } else {
                // for future
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Emoticon> loadStickerData() {
        List<Emoticon> stickers = new ArrayList<>();
        AssetManager assetManager = AppContext.me().getAssets();
        try {
            String[] files = assetManager.list("sticker/" + name);
            for (String file : files) {
                Emoticon emoticon = new Emoticon();
                emoticon.setAssetPath(file);
                stickers.add(emoticon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stickers = stickers;
        return stickers;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
