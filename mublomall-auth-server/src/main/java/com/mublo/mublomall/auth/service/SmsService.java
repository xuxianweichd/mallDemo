/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-09 19:28
 */

package com.mublo.mublomall.auth.service;

import com.mublo.common.utils.to.User;
import com.mublo.mublomall.auth.feign.SmsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author: mublo
 * @Date: 2020/7/9 19:28
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Service
public class SmsService {
    private SmsFeign smsFeign;
    @Autowired
    public SmsService(SmsFeign smsFeign) {
        this.smsFeign = smsFeign;
    }

    public void senCode(String phone) {
        String code= UUID.randomUUID().toString().substring(0,5);
        User user=new User(phone,"亲爱的用户","注册操作",code,5,0);
        smsFeign.reg(user);
    }

}
