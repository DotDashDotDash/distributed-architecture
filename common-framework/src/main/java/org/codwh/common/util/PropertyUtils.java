package org.codwh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils {

    private static Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    private static Map<File, Properties> cache = new HashMap<>();

    public static Properties getProperties(File file) {
        try {
            if (!cache.containsKey(file)) {
                Properties properties = new Properties();
                String content = new String(FileUtils.loadFile(file));
                properties.load(new StringReader(content));
                cache.put(file, properties);
            }
            return cache.get(file);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 从Properties中读取单精度浮点数
     *
     * @param file
     * @param name
     * @return
     */
    public static float getFloat(File file, String name) {
        String value = getString(file, name);
        return ObjectUtils.checkDefault(Float.valueOf(value.trim()), 0).floatValue();
    }

    /**
     * 从文件中读取出指定Properties，并返回该Properties的字符串表示
     *
     * @param file
     * @param name
     * @return
     */
    public static String getString(File file, String name) {
        Properties properties = cache.get(file);
        return ObjectUtils.checkDefault(properties.getProperty(name), null);
    }

    /**
     * 从Properties中读取双精度浮点数
     *
     * @param file
     * @param name
     * @return
     */
    public static Double getDouble(File file, String name) {
        String value = getString(file, name);
        return ObjectUtils.checkDefault(Double.valueOf(value.trim()), 0).doubleValue();
    }

    /**
     * 从Properties中获取整型数字
     *
     * @param file
     * @param name
     * @return
     */
    public static int getInteger(File file, String name) {
        String value = getString(file, name);
        return ObjectUtils.checkDefault(Integer.valueOf(value.trim()), 0);
    }

    /**
     * 清空Properties缓存
     */
    public void clear() {
        cache.clear();
    }
}
