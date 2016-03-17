package io.ganguo.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import io.ganguo.library.exception.BeansException;

/**
 * Bean 工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Beans {

    /**
     * 复制对象
     *
     * @param objSource
     * @param <T>
     * @return
     */
    public static <T> T copyObject(T objSource) throws BeansException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(objSource);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (T) new ObjectInputStream(bais).readObject();
        } catch (Exception e) {
            throw new BeansException("copy object error.", e);
        }
    }

    /**
     * 复制属性值
     *
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = source.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            // get from source
            String name = field.getName();
            Object value = Properties.getProperty(source, name);
            // set to target
            Properties.setProperty(target, name, value);
        }
    }

    /**
     * 是否同一个对象
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean isEquals(Object source, Object target) {
        if (source == null || target == null) {
            return false;
        }
        return source.equals(target);
    }

}
