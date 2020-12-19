package com.hhm.redislockdemo.service;

import com.hhm.redislockdemo.demain.RedissonProperties;
import org.redisson.config.Config;

/**
 * Created by houhaimin on 2020/12/17.
 */
public interface IRedissonConfigService {
    Config create(RedissonProperties properties);
}
