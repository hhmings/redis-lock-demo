package com.hhm.redislockdemo.demain;

/**
 * Created by houhaimin on 2020/12/17.
 */
public class RedissonProperties {

    /**
     * redis 地址，格式: ip:port 多个使用英文【,】分隔
     */
    private String address;

    /**
     * redis 密码
     */
    private String password;

    /**
     * 连接数据库，默认为0数据库
     */
    private int database = 0;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}
