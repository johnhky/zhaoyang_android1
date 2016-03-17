package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.TestBean;

/**
 * Created by Wilson on 10/10/15.
 */
public class PropertiesTest extends ApplicationTest {
    public void testIsReadable() throws Exception {
        assertTrue(Properties.isReadable(new TestBean(), "id"));
        assertTrue(Properties.isReadable(new TestBean(), "name"));
        assertFalse(Properties.isReadable(new TestBean(), "name123321"));
    }

    public void testIsWriteable() throws Exception {
        assertTrue(Properties.isWritable(new TestBean(), "id"));
        assertTrue(Properties.isWritable(new TestBean(), "name"));
        assertFalse(Properties.isWritable(new TestBean(), "name123321"));
    }

    public void testSetProperty() throws Exception {
        TestBean bean = new TestBean();
        bean.setIsReady(false);
        bean.setOk(false);
        Properties.setProperty(bean, "isReady", false);
        Properties.setProperty(bean, "ok", false);
        assertFalse(bean.isReady());
        assertFalse(bean.isOk());

        Properties.setProperty(bean, "id", 899);
        Properties.setProperty(bean, "name", "hi man");
        Properties.setProperty(bean, "isReady", true);
        Properties.setProperty(bean, "ok", true);
        assertEquals(899, bean.getId());
        assertEquals("hi man", bean.getName());
        assertTrue(bean.isReady());
        assertTrue(bean.isOk());

    }

    public void testGetProperty() throws Exception {
        TestBean bean = new TestBean(667, "aha", true, true);
        
        assertEquals(667, Properties.getProperty(bean, "id"));
        assertEquals("aha", Properties.getProperty(bean, "name"));
        assertTrue((boolean) Properties.getProperty(bean, "isReady"));
        assertTrue((boolean) Properties.getProperty(bean, "ok"));
    }
//
//    public void testDescribe() throws Exception {
//        PasswordItem bean = new PasswordItem();
//        bean.setCategory("category");
//        bean.setLauncher("launcher");
//        bean.setNotes("notes");
//        bean.setPassword("password");
//        bean.setTitle("title");
//        bean.setUrl("url");
//        bean.setUser("user");
//        bean.setDefault(true);
//        Map<String, Object> beanMap = Properties.describe(bean);
//        assertEquals(9, beanMap.size());
//        for (Entry<String, Object> e : beanMap.entrySet()) {
//            if (String.class.isAssignableFrom(e.getValue().getClass()))
//                assertEquals(e.getKey(), e.getValue());
//            else if (Boolean.class.isAssignableFrom(e.getValue().getClass()))
//                assertTrue((Boolean) e.getValue());
//        }
//    }
}
