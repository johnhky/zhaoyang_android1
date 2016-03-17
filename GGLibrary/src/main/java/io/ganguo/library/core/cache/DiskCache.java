package io.ganguo.library.core.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

import io.ganguo.library.util.Files;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Disk cache
 * <p/>
 * Created by Tony on 10/5/15.
 */
public class DiskCache implements Cache<String, Serializable> {

    private Logger logger = LoggerFactory.getLogger(DiskCache.class);

    private final ReentrantLock mLock = new ReentrantLock();
    private final File mCachePath;

    public DiskCache(File path) {
        mCachePath = path;
    }

    @Override
    public boolean contains(String key) {
        return get(key) != null;
    }

    @Override
    public Serializable get(String key) {
        CacheItem<String, Serializable> item = read(key);
        // don't exists
        if (item == null) {
            return null;
        }
        // expired
        if (item.isExpired()) {
            remove(key);
            return null;
        }
        // ok
        return item.getValue();
    }

    @Override
    public <T> void getAsync(final String key, final CacheTarget<T> target) {
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {
                final T v = (T) get(key);
                // ui thread
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        target.onLoaded(v);
                    }
                });
            }
        });
    }

    @Override
    public void put(String key, Serializable value) {
        put(key, value, 0);
    }

    @Override
    public void put(String key, Serializable value, int expired) {
        save(key, new CacheItem(key, value, expired));
    }

    @Override
    public void putAsync(String key, Serializable value) {
        putAsync(key, value, 0);
    }

    @Override
    public void putAsync(final String key, final Serializable value, final int expired) {
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {
                put(key, value, expired);
            }
        });
    }

    @Override
    public void remove(String key) {
        File file = getFile(key);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void clear() {
        Files.deleteFiles(mCachePath);
    }

    public File getFile(String key) {
        // key -> hashcode
        String name = String.valueOf(key.hashCode());
        return new File(mCachePath.getAbsolutePath(), name);
    }

    /**
     * 保存缓存文件
     *
     * @param key
     * @param item
     * @return
     */
    private boolean save(String key, CacheItem item) {
        mLock.lock();
        File file = getFile(key);
        try {
            Files.write(file, item);
            return true;
        } catch (IOException e) {
            logger.e("save error.", e);
        } finally {
            mLock.unlock();
        }
        return false;
    }

    /**
     * 读取缓存文件
     *
     * @param key
     * @return
     */
    private CacheItem read(String key) {
        mLock.lock();
        File file = getFile(key);
        if (!file.exists()) {
            return null;
        }
        try {
            return (CacheItem) Files.readSerializable(file);
        } catch (Exception e) {
            logger.e("read error.", e);
        } finally {
            mLock.unlock();
        }
        return null;
    }

}
