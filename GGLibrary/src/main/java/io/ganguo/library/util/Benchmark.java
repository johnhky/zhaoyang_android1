package io.ganguo.library.util;

import java.util.Map;

import io.ganguo.library.ext.map.LRUHashMap;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 一个工具类，可以用来跟踪程序效率问题
 * 目前暂时支持多线程，如果没有end时，会有LRU替换掉
 *
 * @author Tony <6208317@qq.com>
 */
public class Benchmark {
    private static final Logger LOG = LoggerFactory.getLogger(Benchmark.class);
    private static final Map<String, BenchEntry> mBenchStack = new LRUHashMap<>(100);

    /**
     * 开始标记
     *
     * @param tag
     */
    public static void start(String tag) {
        if (Strings.isEmpty(tag)) {
            LOG.w("tag is null");
            return;
        }
        BenchEntry entry = new BenchEntry();
        entry.tag = tag;
        entry.depth = mBenchStack.size();
        entry.start = System.currentTimeMillis();
        mBenchStack.put(tag, entry);
    }

    /**
     * 结束标记
     *
     * @param tag
     */
    public static void end(String tag) {
        if (Strings.isEmpty(tag)) {
            LOG.w("tag is null");
            return;
        }
        BenchEntry entry = mBenchStack.get(tag);
        if (entry == null) {
            LOG.w("Benchmark Not Match, tag spell mistake or forgot Benchmark.end(tag) invoke at somewhere ??");
            return;
        }
        entry.end = System.currentTimeMillis();

        long used = entry.end - entry.start;
        LOG.d("Benchmark [ " + entry.tag + " ] - Used: " + (used) + " ms. ");

        mBenchStack.remove(tag);
    }

    private static class BenchEntry {
        public long depth;
        public long start;
        public long end;
        public String tag;
    }

}
