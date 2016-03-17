package io.ganguo.library.core.cache;

import android.content.Context;

import java.io.File;

/**
 * cache manager
 * <p/>
 * Created by Tony on 10/5/15.
 */
public class CacheManager {

    private static Cache mMemoryCache = null;
    private static DiskCache mDiskCache = null;

    /**
     * memory cache
     *
     * @param context
     * @return
     */
    public static Cache memory(Context context) {
        if (mMemoryCache == null) {
            mMemoryCache = new MemoryCache();
        }
        return mMemoryCache;
    }

    /**
     * disk cache
     *
     * @param context
     * @return
     */
    public static Cache disk(Context context) {
        if (mDiskCache == null) {
            File diskPath = new File(
                    context.getCacheDir().getAbsolutePath() + File.separator + "disk"
            );
            diskPath.mkdirs();
            mDiskCache = new DiskCache(diskPath);
        }
        return mDiskCache;
    }

}
