/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-10 14:02
 */

package com.mublo.mublomall.thirdparty.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;

/**
 * @author: mublo
 * @Date: 2020/7/10 14:02
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
public class MyRedisConfig {
    @Bean(destroyMethod ="shutdown")
    public RedissonClient redisson(){
        Config config=new Config();
        config.useSingleServer().setAddress("redis://192.168.56.10:6379");
        RedissonClient redissonClient= Redisson.create(config);
        return redissonClient;
    }
}
