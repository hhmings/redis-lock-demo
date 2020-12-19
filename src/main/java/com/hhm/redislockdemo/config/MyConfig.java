package com.hhm.redislockdemo.config;

import com.hhm.redislockdemo.core.RedisLock;
import com.hhm.redislockdemo.demain.RedissonProperties;
import com.hhm.redislockdemo.service.IRedissonConfigService;
import com.hhm.redislockdemo.service.impl.SentinelRedissonConfigServiceImpl;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by houhaimin on 2020/12/17.
 */
@Configuration
public class MyConfig {

    @Bean
    public Config sentinelRedissonConfig(){
        RedissonProperties properties = new RedissonProperties();
        properties.setAddress("sentinel-172.22.12.12-6438,172.22.12.14:6440,172.22.12.13:6434,172.22.12.12:6439");
        IRedissonConfigService redissonConfigService = new SentinelRedissonConfigServiceImpl();
        return redissonConfigService.create(properties);
    }

    @Bean(destroyMethod = "shutdown")
    public RedisLock redisLock(){
        RedisLock redisLock = new RedisLock(sentinelRedissonConfig());
        return redisLock;
    }

}
