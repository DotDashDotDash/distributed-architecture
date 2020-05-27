package org.codwh.cache.jvm.heap.off;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.concurrent.TimeUnit;

public class EhCacheExample {

    public static void main(String[] args) {
        //创建一个缓存处理器
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        //缓存创建器
        CacheConfigurationBuilder<String, String> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        String.class,
                        String.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))    //ENTRIES表示缓存容量是根据缓存条目来表示的
                .withDispatcherConcurrency(4)   //并发级别
                .withSizeOfMaxObjectGraph(2)    //统计对象时图遍历的深度
                .withSizeOfMaxObjectSize(1, MemoryUnit.B)   //所有对象大小不能超过1B
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.SECONDS)));

        //创建缓存
        Cache<String, String> cache = cacheManager.createCache("cache1",cacheConfig);

        //创建对象
        Object[] objects = new Object[1024];
        for(int i = 0; i < objects.length; i++){
            objects[i] = new Object();  //这里请思考一个美团面试题：一个Object对象大小是多少
        }

        System.out.println("尝试获取最后一个元素: " + cache.get(String.valueOf(1024)));
        //输出为: null，因为一个Object对象的大小超过了1B，没有被添加进去

        //持续向缓存中添加对象，预设的容量肯定大于可容纳的容量来进行测试
        for(int i = 0; i < objects.length; i++){
            cache.put(String.valueOf(i), objects[i].toString());
        }



        //当关闭JVM的时候，不要忘记忘记调用CacheManager.close()
        cacheManager.close();
    }
}
