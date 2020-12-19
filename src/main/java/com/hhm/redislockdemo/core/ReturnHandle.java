package com.hhm.redislockdemo.core;

/**
 *
 * @author houhaimin
 * @date 2020/12/18
 */
@FunctionalInterface
public interface ReturnHandle<T> {

    /**
     * @return T
     * @throws Exception
     */
    T execute() throws Exception;
}
