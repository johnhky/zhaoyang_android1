package io.ganguo.library.core.cache;

import java.io.Serializable;

/**
 * 缓存Item
 * <p/>
 * Created by Tony on 10/5/15.
 */
public class CacheItem<K, V> implements Serializable {

    /**
     * 缓存key
     */
    private K key;

    /**
     * 缓存值
     */
    private V value;

    /**
     * 过期时间 毫秒
     */
    private long expired;

    public CacheItem(K key, V value, long expired) {
        this.key = key;
        this.value = value;
        if (expired > 0) {
            // now + expired
            this.expired = expired + System.currentTimeMillis();
        }
    }

    /**
     * 获取缓存key
     *
     * @return
     */
    public K getKey() {
        return key;
    }

    /**
     * 获取缓存值
     *
     * @return
     */
    public V getValue() {
        return value;
    }

    /**
     * 缓存是否已经过期
     *
     * @return
     */
    public boolean isExpired() {
        if (expired == 0) {
            return false;
        }
        return this.expired < System.currentTimeMillis();
    }

}
