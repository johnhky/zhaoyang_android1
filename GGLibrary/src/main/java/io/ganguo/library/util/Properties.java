package io.ganguo.library.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 类似于apache 的PropertyUtils
 * 通过反射的方式调用get/set/is方法来操作object中成员变量的值
 * 与{@link java.util.Properties}区分开
 * <p/>
 * https://raw.githubusercontent.com/braiden/fpm2-android/master/app/src/org/braiden/fpm2/util/PropertyUtils.java
 */
public class Properties {

    private static final String METHOD_GET_CLASS = "getClass";
    private static final String GETTER_PREFIX = "get";
    private static final String GETTER_BOOLEAN_PREFIX = "is";
    private static final String SETTER_PREFIX = "set";

    private static final int GETTER = 0;
    private static final int SETTER = 1;

    public static boolean isReadable(Object bean, String property) {
        Validates.notNull(bean);
        Validates.notEmpty(property);
        return findMethod(bean.getClass(), property, GETTER) != null;
    }

    public static boolean isWritable(Object bean, String property) {
        Validates.notNull(bean);
        Validates.notEmpty(property);
        return findMethod(bean.getClass(), property, SETTER) != null;
    }

    public static void setProperty(Object bean, String property, Object value) throws InvocationTargetException, IllegalAccessException {
        Validates.notNull(bean);
        Validates.notEmpty(property);
        Method method = findMethod(bean.getClass(), property, SETTER);
        if (method == null) {
            throw new InvocationTargetException(new NoSuchMethodException());
        }
        method.invoke(bean, value);
    }

    public static Object getProperty(Object bean, String property) throws InvocationTargetException, IllegalAccessException {
        Validates.notNull(bean);
        Validates.notEmpty(property);
        Method method = findMethod(bean.getClass(), property, GETTER);
        if (method == null) {
            throw new InvocationTargetException(new NoSuchMethodException());
        }
        return method.invoke(bean);
    }

    private static Method findMethod(Class<?> clazz, String property, int type) {
        Validates.notNull(clazz);
        Validates.isTrue(type == SETTER || type == GETTER);

        Method result;

        result = getMethod(clazz, GETTER_PREFIX + Strings.capitalize(property));
        if (result == null) {
            //property:boolean ok;
            result = getMethod(clazz, GETTER_BOOLEAN_PREFIX + Strings.capitalize(property));
            result = result != null && (
                    boolean.class.isAssignableFrom(result.getReturnType())
                            || Boolean.class.isAssignableFrom(result.getReturnType()))
                    ? result : null;
        }

        if (result == null && property.startsWith(GETTER_BOOLEAN_PREFIX)) {
            //property:boolean isOk;
            result = getMethod(clazz, property);
            result = result != null && (
                    boolean.class.isAssignableFrom(result.getReturnType())
                            || Boolean.class.isAssignableFrom(result.getReturnType()))
                    ? result : null;
        }
        if (result != null && Void.class.isAssignableFrom(result.getReturnType())) {
            result = null;
        }

        if (result != null && type == SETTER) {
            result = getMethod(clazz, SETTER_PREFIX + Strings.capitalize(property), result.getReturnType());

        }
        return result;
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        Method result = null;
        try {
            result = clazz.getMethod(methodName, params);

            if (result != null && !Modifier.isPublic(result.getModifiers())) {
                result = null;
            }
        } catch (NoSuchMethodException e) {

        }
        return result;
    }


    // TODO: 12/10/15 describe


}