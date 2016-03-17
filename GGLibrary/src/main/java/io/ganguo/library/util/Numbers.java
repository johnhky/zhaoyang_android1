package io.ganguo.library.util;

import java.io.IOException;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 数字处理工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Numbers {

    private static Logger LOG = LoggerFactory.getLogger(Numbers.class);

    /**
     * 对象转 Int
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    /**
     * 对象转 Int
     *
     * @param obj
     * @return 转换异常返回 defValue
     */
    public static int toInt(Object obj, int defValue) {
        if (obj == null) {
            return defValue;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            LOG.e("parser error.", e);
        }
        return defValue;
    }

    /**
     * 对象转 Long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(Object obj) {
        return toLong(obj, 0);
    }

    /**
     * 对象转 Long
     *
     * @param obj
     * @return 转换异常返回 defValue
     */
    public static long toLong(Object obj, long defValue) {
        if (obj == null) {
            return defValue;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            LOG.e("parser error.", e);
        }
        return defValue;
    }

    /**
     * 对象转 Double
     *
     * @param obj
     * @return
     */
    public static double toDouble(Object obj) {
        return toDouble(obj, 0);
    }

    /**
     * 对象转 Double
     *
     * @param obj
     * @return 转换异常返回 defValue
     */
    public static double toDouble(Object obj, double defValue) {
        if (obj == null) {
            return defValue;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            LOG.e("parser error.", e);
        }
        return defValue;
    }

    /**
     * 对象转 Float
     *
     * @param obj
     * @return
     */
    public static float toFloat(Object obj) {
        return toFloat(obj, 0);
    }

    /**
     * 对象转 Float
     *
     * @param obj
     * @return 转换异常返回 defValue
     */
    public static float toFloat(Object obj, float defValue) {
        if (obj == null) {
            return defValue;
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            LOG.e("parser error.", e);
        }
        return defValue;
    }

    /**
     * 如果含小数位 输出小数位，否则去掉小数位
     * # 1.0 -> 1
     * # 1.1111 - > 1.11
     *
     * @param price
     * @return
     */
    public static String toPretty(double price) {
        int intPrice = toInt(price, 0);
        if (intPrice == price) {
            return String.valueOf(intPrice);
        }
        return String.format("%.2f", price);
    }

    /**
     * 一定范围内的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        int range = Math.abs(max - min) + 1;
        return (int) (Math.random() * range) + (min <= max ? min : max);
    }

    /**
     * 数字转中文
     *
     * @param number
     * @return
     * @throws IOException
     */
    public static String toChinese(int number) {
        // 单位数组
        String[] units = new String[]{"十", "百", "千", "万", "十", "百", "千", "亿"};
        // 中文大写数字数组
        String[] numeric = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        String res = "";
        String numberStr = number + "";
        // 遍历一行中所有数字
        for (int k = -1; numberStr.length() > 0; k++) {
            // 解析最后一位
            int j = Integer.parseInt(numberStr.substring(numberStr.length() - 1, numberStr.length()));
            String rtemp = numeric[j];

            // 数值不是0且不是个位 或者是万位或者是亿位 则去取单位
            if (j != 0 && k != -1 || k % 8 == 3 || k % 8 == 7) {
                rtemp += units[k % 8];
            }
            // 拼在之前的前面
            res = rtemp + res;
            // 去除最后一位
            numberStr = numberStr.substring(0, numberStr.length() - 1);
        }
        // 去除后面连续的零零..
        while (res.endsWith(numeric[0])) {
            res = res.substring(0, res.lastIndexOf(numeric[0]));
        }
        // 将零零替换成零
        while (res.indexOf(numeric[0] + numeric[0]) != -1) {
            res = res.replaceAll(numeric[0] + numeric[0], numeric[0]);
        }
        // 将 零+某个单位 这样的窜替换成 该单位 去掉单位前面的零
        for (int m = 1; m < units.length; m++) {
            res = res.replaceAll(numeric[0] + units[m], units[m]);
        }
        return res;
    }

}
