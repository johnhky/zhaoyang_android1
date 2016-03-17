package io.ganguo.library.core.cache;

/**
 * 缓存接口
 * <p/>
 * Created by Tony on 10/5/15.
 */
public interface Cache<K, V> {

    boolean contains(K key);

    V get(K key);

    <T> void getAsync(K key, CacheTarget<T> target);

    void put(K key, V value);

    void put(K key, V value, int expired);

    void putAsync(K key, V value);

    void putAsync(K key, V value, int expired);

    void remove(K key);

    void clear();

}
