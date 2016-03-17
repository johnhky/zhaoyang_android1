package io.ganguo.library.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.TestBean;

/**
 * Created by Tony on 9/30/15.
 */
public class BeansTest extends ApplicationTest implements Serializable {

    public class Bean implements Serializable {
        int id;
        String name;
    }

    public void testCopyObject() throws Exception {
        Bean bean = new Bean();
        bean.id = 1;
        bean.name = "hello";

        Bean copy = Beans.copyObject(bean);

        assertTrue(bean != copy);
        assertEquals(bean.id, copy.id);
        assertEquals(bean.name, copy.name);
    }

    public void testCopyProperties() throws InvocationTargetException, IllegalAccessException {
        TestBean source = new TestBean();
        source.setId(100);
        source.setIsReady(true);
        source.setName("22");

        TestBean target = new TestBean();
        Beans.copyProperties(source, target);

        logger.d(source);
        logger.d(target);
    }
}
