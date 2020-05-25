package org.codwh.redisson.config;

import org.codwh.redisson.util.SpelUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpelConfiguration {

    @Bean
    public SpelUtils spelUtils(){
        return new SpelUtils();
    }
}
