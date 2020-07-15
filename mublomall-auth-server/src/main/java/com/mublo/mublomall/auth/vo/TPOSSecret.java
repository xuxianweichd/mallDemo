/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-12 10:10
 */

package com.mublo.mublomall.auth.vo;

import com.mublo.mublomall.auth.constant.TPOSConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: mublo
 * @Date: 2020/7/12 10:10
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Configuration
@ConfigurationProperties(prefix = "auth.appsecret")
public class TPOSSecret {
    private static String qq;
    private static String wechat;
    private static String microBlog;

    public void setQq(String qq) {
        TPOSSecret.qq = qq;
    }

    public void setWechat(String wechat) {
        TPOSSecret.wechat = wechat;
    }

    public void setMicroBlog(String microBlog) {
        TPOSSecret.microBlog = microBlog;
    }
    public static String getQq() {
        return qq;
    }

    public static String getWechat() {
        return wechat;
    }

    public static String getMicroBlog() {
        return microBlog;
    }
}
