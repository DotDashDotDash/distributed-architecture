package org.codwh.common.util;

import java.util.*;

@SuppressWarnings("unchecked | rawtypes")
public class ObjectUtils {

    public static <T> T cast(Object o) {
        return (T) o;
    }

    public static <T> T checkDefault(T s, T d) {
        return s != null ? s : d;
    }

    /**
     * 链表
     *
     * @param objects
     * @return
     */
    public static List newList(Object... objects) {
        Vector<Object> list = new Vector<>();
        list.addAll(Arrays.asList(objects));
        return list;
    }

    /**
     * 键树
     *
     * @param objects
     * @return
     */
    public static Map newMap(Object... objects) {
        Map<Object, Object> map = new HashMap<>();
        for (int i = 1; i < objects.length; i += 2) {
            map.put(objects[i - 1], objects[i]);
        }
        return map;
    }
}
