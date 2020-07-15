/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-12 08:57
 */

package com.mublo.mublomall.auth.constant;

/**
 * @author: mublo
 * @Date: 2020/7/12 8:57
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
public interface TPOSConstant {
    static String microBlogFront="https://api.weibo.com/oauth2/authorize?client_id=";
    static String microBlogMiddle="&response_type=code&redirect_uri=";
    static String microBlogGetToken="https://api.weibo.com/oauth2/access_token";
}
