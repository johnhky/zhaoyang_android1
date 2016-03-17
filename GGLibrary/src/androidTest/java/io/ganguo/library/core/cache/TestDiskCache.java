package io.ganguo.library.core.cache;

import android.os.SystemClock;

import java.io.Serializable;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Tony on 10/5/15.
 */
public class TestDiskCache extends ApplicationTest implements Serializable {

    private transient Logger logger = LoggerFactory.getLogger(TestDiskCache.class);

    public void testPutAndGet() {
        Bean bean = new Bean();
        bean.id = 1;
        bean.name = "hello";

        Cache cache = CacheManager.disk(getContext());
        cache.put("test1", 123);
        cache.put("test2", "123");
        cache.put("bean", bean);

        assertEquals(cache.get("test1"), 123);
        assertEquals(cache.get("test2"), "123");
        assertNotNull(cache.get("bean"));
    }

    public void testExpired() {
        Bean bean = new Bean();
        bean.id = 1;
        bean.name = "hello";

        Cache cache = CacheManager.disk(getContext());
        cache.put("test1", 123, 100);
        cache.put("test2", bean, 200);

        SystemClock.sleep(110);

        assertFalse(cache.contains("test1"));
        assertTrue(cache.contains("test2"));

        SystemClock.sleep(210);

        assertFalse(cache.contains("test2"));
    }

    public void testAsync() {
        Bean bean = new Bean();
        bean.id = 1;
        bean.name = "hello";

        Cache<String, Serializable> cache = CacheManager.disk(getContext());
        cache.putAsync("123", bean);
        cache.getAsync("123", new CacheTarget<Bean>() {
            @Override
            public void onLoaded(Bean value) {
                assertNotNull(value);
            }
        });

        SystemClock.sleep(100);
    }

    public class Bean implements Serializable {
        public int id;
        public String name;

        @Override
        public String toString() {
            return "Bean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

}
