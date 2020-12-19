package com.hhm.redislockdemo.service.impl;

import com.hhm.redislockdemo.constants.RedissonConstants;
import com.hhm.redislockdemo.demain.RedissonProperties;
import com.hhm.redislockdemo.service.IRedissonConfigService;
import org.redisson.config.Config;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by houhaimin on 2020/12/17.
 */
public class SentinelRedissonConfigServiceImpl implements IRedissonConfigService {

    @Override
    public Config create(RedissonProperties properties) {
        Assert.notNull(properties,"RedissonProperties class is null.");
        Assert.notNull(properties.getAddress(),"RedissonProperties address is null.");

        Config config = new Config();

        String address = properties.getAddress();
        String password = properties.getPassword();
        int database = properties.getDatabase();

        String[] addressArr = address.split(",");
        String sentinelName = addressArr[0];

        config.useSentinelServers().setMasterName(sentinelName)
                .setPingConnectionInterval(3000);
        config.useSentinelServers().setDatabase(database);
        if (!StringUtils.isEmpty(password)){
            config.useSentinelServers().setPassword(password);
        }

        for (int i = 1; i < addressArr.length; i++) {
            config.useSentinelServers().addSentinelAddress(RedissonConstants.REDIS_ADDR_PREFIX+addressArr[i]);
        }
        return config;
    }
}
