package io.ganguo.library.util;

import android.os.SystemClock;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Tony on 10/12/15.
 */
public class BenchmarkTest extends ApplicationTest {

    public void testBenchmark() {
        Tasks.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                Benchmark.start("test");
                SystemClock.sleep(100);
                Benchmark.end("test");
            }
        });
        Tasks.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                Benchmark.start("test2");
                SystemClock.sleep(150);
                Benchmark.end("test2");
            }
        });
        SystemClock.sleep(200);
    }

}
