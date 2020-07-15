/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-10 23:01
 */

package com.mublo.common.utils.constant;

/**
 * @author: mublo
 * @Date: 2020/7/10 23:01
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
public interface messageConstant {
    final static String Null="必须填写";
    final static String nullUserMsg="用户名"+Null;
    final static String nullPhoneMsg="手机号"+Null;
    final static String nullPasswordMsg="密码"+Null;
    final static String nullCodeMsg="验证码"+Null;
    final static String nullEmailMsg="邮箱"+Null;
    final static String Exist="已存在";
    final static String existUserMsg="用户名"+Exist;
    final static String existPhoneMsg="手机号"+Exist;
    final static String existEmailMsg="邮箱"+Exist;
    final static String unEqualPasswordMsg="两次密码不一致";
    final static String errorCodeMsg="验证码不正确";
    final static String errorRegExpPhoneMsg="手机号格式不正确";
    final static String errorRegExpPasswordMsg="密码格式不正确（大于8位小于16位）必须使用英文加数字结合";
}
