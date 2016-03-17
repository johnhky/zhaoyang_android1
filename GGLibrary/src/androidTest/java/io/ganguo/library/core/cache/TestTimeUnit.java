package io.ganguo.library.core.cache;

import java.util.concurrent.TimeUnit;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Tony on 10/7/15.
 */
public class TestTimeUnit extends ApplicationTest {

    public void test() {
        // 10s
        long millis = TimeUnit.SECONDS.toMillis(10);

        System.out.println(millis);
        System.out.println(System.currentTimeMillis());
    }

}
