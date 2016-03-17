package io.ganguo.library.ext.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRUHashMap
 * <p/>
 * Created by Tony on 10/12/15.
 */
public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

    /**
     * 容量
     */
    private final int capacity;

    /**
     * 按容量构造
     *
     * @param capacity
     */
    public LRUHashMap(int capacity) {
        super(capacity + 1, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * 删除规则
     *
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

}