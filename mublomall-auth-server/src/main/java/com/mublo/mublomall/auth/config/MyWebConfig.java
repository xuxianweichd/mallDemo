/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-08 12:39
 */

package com.mublo.mublomall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: mublo
 * @Date: 2020/7/8 12:39
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    /**
     * 视图映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/reg").setViewName("reg");
    }
}
