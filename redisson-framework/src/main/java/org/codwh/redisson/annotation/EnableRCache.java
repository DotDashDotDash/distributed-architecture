package org.codwh.redisson.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRCache {

    /**
     * 缓存的名称，当一个方法上含有@Cacheable, @CachePut, @CacheEvict注解
     * 的时候，必须将对应的 methodName 放置在这个数组中
     *
     * @return
     */
    String[] cacheNames();

    /**
     * 缓存过期的时间，默认30min
     *
     * @return
     */
    long ttl() default 1000 * 60 * 30;

    /**
     * 缓存的最大空闲时间，默30min
     *
     * @return
     */
    long maxIdleTime() default 1000 * 60 * 30;
}
