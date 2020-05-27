package org.codwh.cache.disk;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class EhDiskCacheExample {

    public static void main(String[] args) {
        //创建一个缓存管理器
        CacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder()
                .using(PooledExecutionServiceConfigurationBuilder
                    .newPooledExecutionServiceConfigurationBuilder()
                    .defaultPool("default", 1, 10).build())
                .with(new CacheManagerPersistenceConfiguration(new File("D:\\MyGithub\\repository\\distributed-architecture\\cache\\disk-cache\\src\\main\\resources\\cache.bak")))
                .build(true);

        //缓存配置
        CacheConfigurationBuilder<String, String> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        String.class,
                        String.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().disk(100, MemoryUnit.MB, true))  //磁盘缓存
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(50, TimeUnit.SECONDS)))
                .withSizeOfMaxObjectGraph(3)
                .withSizeOfMaxObjectSize(1, MemoryUnit.MB);

        Cache<String, String> cache = cacheManager.createCache("cache1",cacheConfig);

        //对象数组
        Object[] objects = new Object[1024];
        for(int i = 0; i < objects.length; i++){
            objects[i] = new Object();
        }

        //添加对象
        for(int i = 0; i < objects.length; i++){
            cache.put(String.valueOf(i), objects[i].toString());
        }


        //JVM关闭时自动持久化到磁盘
        cacheManager.close();
    }
}
