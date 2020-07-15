/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-12 17:16
 */

package com.mublo.mublomall.auth.to;

import lombok.Data;

/**
 * @author: mublo
 * @Date: 2020/7/12 17:16
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 * 微博授权回调信息
 * @access_token 授权码
 * @expires_in 授权码持续时间（秒）
 * @uid 用户id
 */
@Data
public class TPOSMicroBlogMandateTo {
    private String access_token;
    private String expires_in;
    private Long uid;
}
