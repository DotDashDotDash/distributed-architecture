package org.codwh.common.util;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    /**
     * 判断数组中是否含有指定元素
     *
     * @param array
     * @param element
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> boolean isContains(T[] array, T element) throws Exception {
        if (array == null || element == null) {
            return false;
        }
        if (!array.getClass().getComponentType().equals(element.getClass())) {
            throw new IllegalArgumentException("数组与元素类型不符。");
        }
        List<T> list = Arrays.asList(array);
        return list.contains(element);
    }

    /**
     * 将数组用特殊的连接符连接成一个字符串
     *
     * @param array
     * @param linkStr
     * @return
     */
    public static String join(Object[] array, String linkStr) {
        if (array != null && array.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (Object obj : array) {
                if (sb.length() > 0) {
                    sb.append(linkStr);
                }
                sb.append(obj);
            }
            return sb.toString();
        }
        return "";
    }
}
