package io.ganguo.library.util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class StringsUtils {

    /**
     * URL正则
     */
    public final static Pattern URL_PATTERN = Pattern
            .compile("[a-zA-z]+://[^\\s]*");

    /**
     * 邮政编码正则
     */
    public final static Pattern ZIP_PATTERN = Pattern
            .compile("[1-9]\\d{5}(?!\\d)");

    /**
     * 身份证正则
     */
    public final static Pattern IDCARD_PATTERN = Pattern
            .compile("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)");

    /**
     * 手机号码正则
     */
    public final static Pattern MOBILE_PATTERN = Pattern
            .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$");

    /**
     * 邮箱正则
     */
    public final static Pattern EMAIL_PATTERN = Pattern
            .compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");

    public final static Pattern PHONE_PATTERN = Pattern.compile(
            "^0?(10|(2|3[1,5,7]|4[1,5,7]|5[1,3,5,7]|7[1,3,5,7,9]|8[1,3,7,9])[0-9]|91[0-7,9]|(43|59|85)[1-9]|39[1-8]|54[3,6]|(701|580|349|335)|54[3,6]|69[1-2]|44[0,8]|48[2,3]|46[4,7,8,9]|52[0,3,7]|42[1,7,9]|56[1-6]|63[1-5]|66[0-3,8]|72[2,4,8]|74[3-6]|76[0,2,3,5,6,8,9]|82[5-7]|88[1,3,6-8]|90[1-3,6,8,9])\\d{7,8}$");

    public final static Pattern MOBILE_OR_PHONE_PATTERN = Pattern.compile(
            "(^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$)|" +
                    "(^0?(10|(2|3[1,5,7]|4[1,5,7]|5[1,3,5,7]|7[1,3,5,7,9]|8[1,3,7,9])[0-9]|91[0-7,9]|(43|59|85)[1-9]|39[1-8]|54[3,6]|(701|580|349|335)|54[3,6]|69[1-2]|44[0,8]|48[2,3]|46[4,7,8,9]|52[0,3,7]|42[1,7,9]|56[1-6]|63[1-5]|66[0-3,8]|72[2,4,8]|74[3-6]|76[0,2,3,5,6,8,9]|82[5-7]|88[1,3,6-8]|90[1-3,6,8,9])\\d{7,8}$)");



    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String s : str) {
            if (s == null || s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String... str) {
        if (str == null) {
            return false;
        }
        for (String s : str) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一样的
     */
    public static boolean isEquals(String src, String target) {
        if (isEmpty(src, target)) {
            return false;
        }
        return src.equals(target);
    }

    /**
     * 判断是不是一样的
     */
    public static boolean isEqualsIgnoreCase(String src, String target) {
        if (isEmpty(src, target)) {
            return false;
        }
        return src.equalsIgnoreCase(target);
    }

    /**
     * 以一种简单的方式格式化字符串
     * 例如 String s = StringsUtils.format("{0} is {1}", "apple", "fruit");
     * 输出 apple is fruit.
     *
     * @param pattern
     * @param args
     * @return
     */
    public static String format(String pattern, Object... args) {
        for (int i = 0; i < args.length; i++) {
            pattern = pattern.replace("{" + i + "}", args[i].toString());
        }
        return pattern;
    }

    /**
     * 判断是不是电子邮件地址
     *
     * @param text
     * @return
     */
    public static boolean isEmail(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(text).matches();
    }

    /**
     * 判断是不是URL地址
     *
     * @param text
     * @return
     */
    public static boolean isUrl(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return URL_PATTERN.matcher(text).matches();
    }

    /**
     * 判断是不是手机号码
     *
     * @param text
     * @return
     */
    public static boolean isMobile(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return MOBILE_PATTERN.matcher(text).matches();
    }

    /**
     * 判断是不是身份证号码
     *
     * @param text
     * @return
     */
    public static boolean isIDCard(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return IDCARD_PATTERN.matcher(text).matches();
    }

    /**
     * 判断是不是邮政编码
     *
     * @param text
     * @return
     */
    public static boolean isZipCode(String text) {
        if (isEmpty(text)) {
            return false;
        }
        return ZIP_PATTERN.matcher(text).matches();
    }

    /**
     * 随机的UUID
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 首字母变为大写，其他保持不变
     *
     * @param str
     * @return
     */
    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        final char newChar = Character.toUpperCase(firstChar);
        if (firstChar == newChar) {
            // already capitalized
            return str;
        }

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        str.getChars(1, strLen, newChars, 1);
        return String.valueOf(newChars);
    }


}
