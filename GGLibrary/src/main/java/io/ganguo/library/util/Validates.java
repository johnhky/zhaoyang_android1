package io.ganguo.library.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Wilson on 10/10/15.
 */
public class Validates {
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_NOT_EMPTY_STRING_EX_MESSAGE = "The validated string is empty";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";

    // isTrue boolean
    //---------------------------------------------------------------------------------

    public static void isTrue(final boolean expression) {
        if (expression == false) {
            throw new IllegalArgumentException(DEFAULT_IS_TRUE_EX_MESSAGE);
        }
    }

    // notNull object
    //---------------------------------------------------------------------------------

    /**
     * 检查object是否为null, null则抛出{@link NullPointerException}
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> T notNull(final T object) {
        return notNull(object, DEFAULT_IS_NULL_EX_MESSAGE);
    }

    /**
     * 检查object是否为null, null则抛出{@link NullPointerException}
     *
     * @param object
     * @param message
     * @param values
     * @param <T>
     * @return
     */
    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }

    // notEmpty array
    //---------------------------------------------------------------------------------

    /**
     * 数组是否为null或者长度为0
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE);
    }

    /**
     * 数组是否为null或者长度为0
     *
     * @param array
     * @param message
     * @param values
     * @param <T>
     * @return
     */
    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        if (array == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }

    // notEmpty collection
    //---------------------------------------------------------------------------------

    /**
     * 集合是否为null或者长度为0
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE);
    }

    /**
     * 集合是否为null或者长度为0
     *
     * @param collection
     * @param message
     * @param values
     * @param <T>
     * @return
     */
    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    // notEmpty map
    //---------------------------------------------------------------------------------

    /**
     * map是否为null或者长度为0
     *
     * @param map
     * @param <T>
     * @return
     */
    public static <T extends Map<?, ?>> T notEmpty(final T map) {
        return notEmpty(map, DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE);
    }

    /**
     * map是否为null或者长度为0
     *
     * @param map
     * @param message
     * @param values
     * @param <T>
     * @return
     */
    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        if (map == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    // notEmpty string
    //---------------------------------------------------------------------------------

    /**
     * 字符串是否为null/空字符串/空格
     *
     * @param str
     * @param <T>
     * @return
     */
    public static <T extends String> T notEmpty(final T str) {
        return notEmpty(str, DEFAULT_NOT_EMPTY_STRING_EX_MESSAGE);
    }

    /**
     * 字符串是否为null/空字符串/空格
     *
     * @param str
     * @param message
     * @param values
     * @param <T>
     * @return
     */
    public static <T extends String> T notEmpty(final T str, final String message, final Object... values) {
        if (str == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (str == null || str.isEmpty() || str.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return str;
    }


}
