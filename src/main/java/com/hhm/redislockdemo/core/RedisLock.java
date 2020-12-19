package com.hhm.redislockdemo.core;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by houhaimin on 2020/12/18.
 */

public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private RedissonClient redissonClient;

    public RedisLock(Config config) {
        this.redissonClient = Redisson.create(config);
    }

    /**
     * 有返回值的加锁
     * @param lockName 锁名称（key+业务数据）
     * @param handle
     * @param <T> 返回对象
     * @return
     */
    public <T> T lock(String lockName,ReturnHandle<T> handle) throws Exception {
        return lockWithResult(lockName,-1L,handle);
    }

    /**
     * 有返回值的加锁
     * @param lockName 锁名称（key+业务数据）
     * @param leaseTime 锁有效时间
     * @param handle
     * @param <T> 返回对象
     * @return
     */
    public <T> T lock(String lockName,long leaseTime,ReturnHandle<T> handle) throws Exception {
        return lockWithResult(lockName,leaseTime,handle);
    }

    /**
     * 有返回值的连接超时的加锁
     * @param lockName 锁名称（key+业务数据）
     * @param leaseTime 锁有效时间
     * @param handle
     * @param <T> 回对象
     * @return
     * @throws Exception
     */
    public <T> T tryLock(String lockName, long leaseTime,ReturnHandle<T> handle) throws Exception {
        return tryLockWithResult(lockName,leaseTime,-1L,handle);
    }

    /**
     * 有返回值的连接超时的加锁
     * @param lockName 锁名称（key+业务数据）
     * @param leaseTime 锁有效时间
     * @param waitTime 连接超时时间
     * @param handle
     * @param <T> 回对象
     * @return
     * @throws Exception
     */
    public <T> T tryLock(String lockName, long leaseTime,long waitTime,ReturnHandle<T> handle) throws Exception {
        return tryLockWithResult(lockName,leaseTime,waitTime,handle);
    }

    /**
     * 判断该锁是否已经被线程持有
     * @param lockName  锁名称
     */
    public boolean isLock(String lockName) {
        RLock rLock = getLock(lockName);
        return rLock.isLocked();
    }

    /**
     * 判断该线程是否持有当前锁
     * @param lockName  锁名称
     */
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = getLock(lockName);
        return rLock.isHeldByCurrentThread();
    }

    public void shutdown(){
        this.redissonClient.shutdown();
    }

    //----------------------- private method --------------------------------

    private <T> T tryLockWithResult(String lockName, long leaseTime,long waitTime,ReturnHandle<T> handle) throws Exception {
        RLock lock = getLock(lockName);
        try {
            lock.tryLock(waitTime,leaseTime,TimeUnit.SECONDS);
            logger.info("redis tryLock has return acquire succ. lockName:{},leaseTime:{},waitTime:{}",lockName,leaseTime,waitTime);
            return handle.execute();
        }finally {
            lock.unlock();
            logger.info("redis tryLock has return release succ. lockName:{},leaseTime:{},waitTime:{}",lockName,leaseTime,waitTime);
        }
    }

    private <T> T lockWithResult(String lockName,long leaseTime,ReturnHandle<T> handle) throws Exception {
        RLock lock = getLock(lockName);
        try {
            lock.lock(leaseTime, TimeUnit.SECONDS);
            logger.info("redis lock has return acquire succ. lockName:{},leaseTime:{}",lockName,leaseTime);
            return handle.execute();
        } finally {
            lock.unlock();
            logger.info("redis lock has return release succ. lockName:{},leaseTime:{}",lockName,leaseTime);
        }
    }

    private RLock getLock(String lockName){
        if (StringUtils.isEmpty(lockName)){
            throw new RuntimeException("acquire redis lock lockName is null.");
        }
        logger.info("redis lock start, lockName:{}",lockName);
        return this.redissonClient.getLock(lockName);
    }
}
