package io.ganguo.library.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memory cache
 * <p/>
 * Created by Tony on 10/5/15.
 */
public class MemoryCache implements Cache<Object, Object> {

    private Map<Object, CacheItem<Object, Object>> mCaches = new ConcurrentHashMap<>();

    @Override
    public boolean contains(Object key) {
        return get(key) != null;
    }

    @Override
    public Object get(Object key) {
        CacheItem item = mCaches.get(key);
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
    public <T> void getAsync(final Object key, final CacheTarget<T> target) {
        target.onLoaded((T) get(key));
    }

    @Override
    public void put(Object key, Object value) {
        put(key, value, 0);
    }

    @Override
    public void put(Object key, Object value, int expired) {
        mCaches.put(key, new CacheItem(key, value, expired));
    }

    @Override
    public void putAsync(Object key, Object value) {
        put(key, value);
    }

    @Override
    public void putAsync(Object key, Object value, int expired) {
        put(key, value, expired);
    }

    @Override
    public void remove(Object key) {
        mCaches.remove(key);
    }

    @Override
    public void clear() {
        mCaches.clear();
    }

}
