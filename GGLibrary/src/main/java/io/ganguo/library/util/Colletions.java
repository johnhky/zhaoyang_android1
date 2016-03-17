package io.ganguo.library.util;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具
 * 名字少了个c, 与{@link java.util.Collections}区分开
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Colletions {

    /**
     * 集合是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 数组是否为空
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(final T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 集合是否不为空
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 集合是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || isEmpty(map.keySet());
    }

    /**
     * Map是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


}
