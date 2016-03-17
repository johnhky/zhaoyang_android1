package io.ganguo.library.core.cache;

import android.os.SystemClock;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Tony on 10/5/15.
 */
public class TestMemoryCache extends ApplicationTest {

    public void testPutAndGet() {
        Cache cache = CacheManager.memory(getContext());
        cache.put("test1", 123);
        cache.put("test2", "123");

        assertEquals(cache.get("test1"), 123);
        assertEquals(cache.get("test2"), "123");
    }

    public void testExpired() {
        Cache cache = CacheManager.memory(getContext());
        cache.put("test1", 123, 100);
        cache.put("test2", "123", 200);

        SystemClock.sleep(110);

        assertFalse(cache.contains("test1"));
        assertTrue(cache.contains("test2"));

        SystemClock.sleep(210);

        assertFalse(cache.contains("test2"));
    }

}
