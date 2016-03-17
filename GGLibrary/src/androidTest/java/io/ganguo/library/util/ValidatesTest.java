package io.ganguo.library.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.TestBean;

/**
 * Created by Wilson on 10/10/15.
 */
public class ValidatesTest extends ApplicationTest {

    public void testIsTrue() {
        Validates.isTrue(true);
    }

    public void testNotNull() {
        TestBean testBean = new TestBean();

        Validates.notNull(testBean);
        Validates.notNull(testBean, "TestBean cannot be null");
        Validates.notNull(testBean, "TestBean cannot be null, currentTime:%s, number:%s", System.currentTimeMillis(), 3.1415926);
    }

    public void testNotEmptyArray() {
        String[] arr = {"1", "a", "#"};
        Validates.notEmpty(arr);
        Validates.notEmpty(arr, "array cannot be empty");
        Validates.notEmpty(arr, "array cannot be empty, currentTime:%s", System.currentTimeMillis());
    }

    public void testNotEmptyCollection() {
        List<String> list = new ArrayList<String>() {
            {
                add("1");
                add("a");
                add("#");
            }
        };
        Validates.notEmpty(list);
        Validates.notEmpty(list, "list cannot be empty");
        Validates.notEmpty(list, "list cannot be empty, currentTime:%s", System.currentTimeMillis());
    }

    public void testNotEmptyMap() {
        Map<String, String> map = new HashMap<String, String>() {{
            put("0", "a");
            put("1", "b");
            put("2", "c");
        }};
        Validates.notEmpty(map);
        Validates.notEmpty(map, "map cannot be empty");
        Validates.notEmpty(map, "map cannot be empty, currentTime:%s", System.currentTimeMillis());
    }

    public void testNotEmptyString() {
        String str = new String("handsome dog");
        Validates.notEmpty(str);
        Validates.notEmpty(str, "string cannot be empty");
        Validates.notEmpty(str, "strings cannot be empty, currentTime:%s", System.currentTimeMillis());
    }


}
