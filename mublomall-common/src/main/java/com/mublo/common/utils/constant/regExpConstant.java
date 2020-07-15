/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-10 23:05
 */

package com.mublo.common.utils.constant;

/**
 * @author: mublo
 * @Date: 2020/7/10 23:05
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */

/**
 * 正则表达式
 */
public interface regExpConstant {
    final static String phoneRegExp="^1[3|4|5|6|7|8|9][0-9]\\d{8}$";
    final static String passwordRegExp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
}
