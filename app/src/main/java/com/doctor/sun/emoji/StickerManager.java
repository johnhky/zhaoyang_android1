package com.doctor.sun.emoji;

import android.content.res.AssetManager;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.doctor.sun.AppContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 贴图管理类
 */
public class StickerManager {
    public static final int PER_PAGE = 8;
    public static final String FILE_ANDROID_ASSET_STICKER = "file:///android_asset/sticker/";

    private SparseArray<List<Emoticon>> datas = new SparseArray<>();
    private SparseIntArray pageCounts = new SparseIntArray();

    private static StickerManager instance;

    public static StickerManager getInstance() {
        if (instance == null) {
            instance = new StickerManager();
            instance.loadStickerCategory();
        }
        return instance;
    }

    public List<Emoticon> emoticons(int page) {
        return datas.get(page - EmoticonManager.getPageCount());
    }

    public void loadStickerCategory() {
        AssetManager assetManager = AppContext.me().getAssets();
        try {
            String[] files = assetManager.list("sticker");
            for (int i = 0; i < files.length; i++) {
                String name = files[i];
                List<Emoticon> emoticons = loadStickerData(name);
                for (int size = 0; size < emoticons.size(); size += PER_PAGE) {

                    int end = size + PER_PAGE;
                    end = end < emoticons.size() ? end : emoticons.size();

                    this.datas.put(this.datas.size(), emoticons.subList(size, end));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Emoticon> loadStickerData(String name) {
        List<Emoticon> stickers = new ArrayList<>();
        AssetManager assetManager = AppContext.me().getAssets();
        try {
            String[] files = assetManager.list("sticker/" + name);

            int pageCount = files.length / PER_PAGE;
            pageCount += files.length % PER_PAGE == 0 ? 0 : 1;

            pageCounts.put(pageCounts.size(), pageCount);
            for (String file : files) {
                Emoticon emoticon = new Emoticon();
                emoticon.setId(name);
                emoticon.setTag(file);
                emoticon.setAssetPath(FILE_ANDROID_ASSET_STICKER + name + "/" + file);
                stickers.add(emoticon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stickers;
    }

    public int getTotalPage() {
        return datas.size();
    }

    public int getPageCount(int type) {
        return pageCounts.get(type);
    }

    public int getPagesBefore(int type) {
        int result = 0;
        for (int i = type; i >= 0; i--) {
            result += pageCounts.get(i);
        }
        return result;
    }
}
