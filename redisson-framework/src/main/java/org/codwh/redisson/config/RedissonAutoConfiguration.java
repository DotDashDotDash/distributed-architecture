package org.codwh.redisson.config;

import com.sun.org.apache.bcel.internal.util.ClassPath;
import org.codwh.redisson.property.RedissonProperties;
import org.codwh.redisson.util.RedissonUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Configuration
@SuppressWarnings("all")
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties({RedisProperties.class, RedissonProperties.class})
public class RedissonAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private ApplicationContext ctx;

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean
    public RedissonUtils redissonUtils(){
        return new RedissonUtils();
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedissonConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedissonConnectionFactory redisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(){
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
        int timeout;
        if(timeoutValue == null){
            timeout = 1000 * 10;    //default 10s
        }else if(timeoutValue instanceof Integer){
            timeout = (Integer) timeoutValue;
        }else{
            Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
            timeout = (((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue());
        }

        Config config = null;
        String s = redissonProperties.getConfig();
        if(redissonProperties.getConfig() != null){ //优先从用户配置文件中读
            InputStream configStream = getConfigStream();
            try {
                config = Config.fromYAML(configStream);
            }catch (IOException e){
                try {
                    config = Config.fromJSON(configStream);
                }catch (IOException ee){
                    throw new RuntimeException("Redisson配置文件的格式必须是YAML或者JSON.");
                }
            }
        }else{  //如果用户没有配置redisson，将从redis配置文件中尝试去配置redisson
            if(redisProperties.getSentinel() != null){  //哨兵模式
                Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
                Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

                String[] nodes;
                if(nodesValue == null){
                    throw new RuntimeException("Redis哨兵模式下必须要有nodes节点配置");
                }else{
                    if(nodesValue instanceof String){
                        nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
                    }else{
                        nodes = convert((List<String>) nodesValue);
                    }
                }

                config = new Config();
                config
                        .useSentinelServers()
                        .addSentinelAddress(nodes)
                        .setMasterName(redisProperties.getSentinel().getMaster())
                        .setDatabase(redisProperties.getDatabase())
                        .setPassword(redisProperties.getPassword())
                        .setConnectTimeout(timeout);
            }else if(clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null){
                //集群模式
                Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
                Method nodeMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
                List<String> nodesList = (List<String>) ReflectionUtils.invokeMethod(nodeMethod, clusterObject);

                String[] nodes;
                if(nodesList == null){
                    throw new RuntimeException("Redis集群模式下必须要有nodes节点配置");
                }else{
                    nodes = convert(nodesList);
                }

                config = new Config();
                config
                        .useClusterServers()
                        .addNodeAddress(nodes)
                        .setConnectTimeout(timeout)
                        .setPassword(redisProperties.getPassword());
            }else{
                config = new Config();
                String prefix = "redis://";
                Method isSslMethod = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
                if(isSslMethod != null && ReflectionUtils.invokeMethod(isSslMethod, redisProperties) == Boolean.TRUE){
                    prefix = "rediss://";   //use ssl
                }

                config
                        .useSingleServer()
                        .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                        .setDatabase(redisProperties.getDatabase())
                        .setPassword(redisProperties.getPassword());
            }
        }
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    public Config config(){
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
        int timeout;
        if(timeoutValue == null){
            timeout = 1000 * 10;    //default 10s
        }else if(timeoutValue instanceof Integer){
            timeout = (Integer) timeoutValue;
        }else{
            Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
            timeout = (((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue());
        }

        Config config = null;
        String s = redissonProperties.getConfig();
        if(redissonProperties.getConfig() != null){ //优先从用户配置文件中读
            InputStream configStream = getConfigStream();
            try {
                config = Config.fromYAML(configStream);
            }catch (IOException e){
                try {
                    config = Config.fromJSON(configStream);
                }catch (IOException ee){
                    throw new RuntimeException("Redisson配置文件的格式必须是YAML或者JSON.");
                }
            }
        }else{  //如果用户没有配置redisson，将从redis配置文件中尝试去配置redisson
            if(redisProperties.getSentinel() != null){  //哨兵模式
                Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
                Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

                String[] nodes;
                if(nodesValue == null){
                    throw new RuntimeException("Redis哨兵模式下必须要有nodes节点配置");
                }else{
                    if(nodesValue instanceof String){
                        nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
                    }else{
                        nodes = convert((List<String>) nodesValue);
                    }
                }

                config = new Config();
                config
                        .useSentinelServers()
                        .addSentinelAddress(nodes)
                        .setMasterName(redisProperties.getSentinel().getMaster())
                        .setDatabase(redisProperties.getDatabase())
                        .setPassword(redisProperties.getPassword())
                        .setConnectTimeout(timeout);
            }else if(clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null){
                //集群模式
                Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
                Method nodeMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
                List<String> nodesList = (List<String>) ReflectionUtils.invokeMethod(nodeMethod, clusterObject);

                String[] nodes;
                if(nodesList == null){
                    throw new RuntimeException("Redis集群模式下必须要有nodes节点配置");
                }else{
                    nodes = convert(nodesList);
                }

                config = new Config();
                config
                        .useClusterServers()
                        .addNodeAddress(nodes)
                        .setConnectTimeout(timeout)
                        .setPassword(redisProperties.getPassword());
            }else{
                config = new Config();
                String prefix = "redis://";
                Method isSslMethod = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
                if(isSslMethod != null && ReflectionUtils.invokeMethod(isSslMethod, redisProperties) == Boolean.TRUE){
                    prefix = "rediss://";   //use ssl
                }

                config
                        .useSingleServer()
                        .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                        .setDatabase(redisProperties.getDatabase())
                        .setPassword(redisProperties.getPassword());
            }
        }
        return config;
    }

    private String[] convert(List<String> nodesList){
        String[] nodes = new String[nodesList.size()];
        int nodeIndex = 0;
        for(String node : nodesList){
            if(!node.startsWith("reids://") && !node.startsWith("rediss://")){
                nodes[nodeIndex++] = "redis://" + node;
            }else{
                nodes[nodeIndex++] = node;
            }
        }
        return nodes;
    }

    private InputStream getConfigStream(){
        try {
            Resource resource = ctx.getResource(redissonProperties.getConfig());
            InputStream is = resource.getInputStream();
            return is;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
