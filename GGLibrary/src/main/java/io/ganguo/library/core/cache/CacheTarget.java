package io.ganguo.library.core.cache;

/**
 * Created by Tony on 10/5/15.
 */
public interface CacheTarget<V> {

    void onLoaded(V value);

}
