package io.ganguo.library.core.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import io.ganguo.library.BaseApp;
import io.ganguo.library.Config;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Glide 模块
 * <p/>
 * https://github.com/bumptech/glide
 * <p/>
 * Created by Tony on 9/9/15.
 */
public class GGlide implements GlideModule {
    private Logger logger = LoggerFactory.getLogger(GGlide.class);
    private static int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 1024; // 1G

    private static Glide mGlide;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.

        String imageCache = Config.getImageCachePath().getAbsolutePath();
        builder.setDiskCache(new DiskLruCacheFactory(imageCache, DEFAULT_DISK_CACHE_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        logger.d("applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
        mGlide = glide;

        logger.d("registerComponents");
    }

    /**
     * get glide
     *
     * @return
     */
    public static Glide getGlide() {
        if (mGlide == null) {
            return Glide.get(BaseApp.me());
        }
        return mGlide;
    }

    /**
     * 从缓存池获取bitmap
     *
     * @param width
     * @param height
     * @param config
     * @return
     */
    public static Bitmap getBitmap(int width, int height, Bitmap.Config config) {
        BitmapPool bitmapPool = getGlide().getBitmapPool();
        Bitmap bitmap = bitmapPool.get(width, height, config);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, config);
        }
        return bitmap;
    }

    /**
     * 需要recycle都存入这里
     *
     * @param bitmap
     */
    public static void putBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        BitmapPool bitmapPool = getGlide().getBitmapPool();
        bitmapPool.put(bitmap);
    }

    /**
     * 需要recycle都存入这里
     *
     * @param bitmaps
     */
    public static void putBitmap(Bitmap... bitmaps) {
        for (Bitmap bitmap : bitmaps) {
            putBitmap(bitmap);
        }
    }

    /**
     * 清除内存缓存
     */
    public static void clearMemory() {
        getGlide().clearMemory();
    }

}