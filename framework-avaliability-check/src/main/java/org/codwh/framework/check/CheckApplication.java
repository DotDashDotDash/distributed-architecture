package org.codwh.framework.check;

import org.codwh.redisson.annotation.DLock;
import org.codwh.redisson.property.RedissonProperties;
import org.codwh.redisson.util.RedissonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(RedissonProperties.class)
public class CheckApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RedissonUtils redissonUtils;

    public static void main(String[] args) {
        SpringApplication.run(CheckApplication.class, args);
    }

    public void run(String... args) throws Exception {
        doDLock();
    }

    @DLock(locks = {"dlock", "fk"}, waitLockTimeout = 1000 * 10, watchDogTimeout = 1000 * 10)
    public void doDLock(){

    }
}
