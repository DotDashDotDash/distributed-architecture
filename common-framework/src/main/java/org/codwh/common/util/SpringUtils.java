package org.codwh.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return SpringUtils.applicationContext;
    }

    /**
     * 获取Spring容器中的bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据beanName和Class类型获取Spring容器中的bean
     *
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return (T) applicationContext.getBean(beanName, clazz);
    }

    /***
     * 检查Spring容器中是否含有对应beanName的bean
     *
     * @param beanName
     * @return
     */
    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    /**
     * 检查Spring容器中beanName对应的bean是否是单例模式
     *
     * @param beanName
     * @return
     */
    public static boolean isSingleton(String beanName) {
        return applicationContext.isSingleton(beanName);
    }

    /**
     * 获取bean种类
     *
     * @param beanName
     * @return
     */
    public static Class<?> getBeanType(String beanName) {
        return applicationContext.getType(beanName);
    }

    /**
     * 如果bean定义的有别名，返回这些别名
     *
     * @param beanName
     * @return
     */
    public static String[] getAliases(String beanName) {
        return applicationContext.getAliases(beanName);
    }
}
