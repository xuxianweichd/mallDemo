/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:40
 */

package com.mublo.mublomall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: mublo
 * @Date: 2020/7/7 17:40
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Component
@Data
@ConfigurationProperties(prefix = "mublomall.thread")
public class ThreadPoolProperties {
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveTime;
}
