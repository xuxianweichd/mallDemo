/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-13 03:54
 */

package com.mublo.mublomall.product.config;

/**
 * @author: mublo
 * @Date: 2020/7/13 3:54
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class MySessionCookieConfig {
    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer defaultCookieSerializer=new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName("mubloMallSession");
        defaultCookieSerializer.setDomainName("mublomall.com");
        return defaultCookieSerializer;
    }

    @Bean(initMethod = "")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        //TODO 带有@autoType关键字的不放反序列化待解决
//        return new GenericFastJsonRedisSerializer();
        return new GenericJackson2JsonRedisSerializer();
    }

}
