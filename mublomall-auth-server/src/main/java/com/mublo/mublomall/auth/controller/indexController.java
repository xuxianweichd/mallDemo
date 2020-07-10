/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 18:41
 */

package com.mublo.mublomall.auth.controller;

import com.mublo.common.utils.R;
import com.mublo.mublomall.auth.feign.SmsFeign;
import com.mublo.mublomall.auth.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: mublo
 * @Date: 2020/7/7 18:41
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Controller
@RefreshScope
public class indexController {
    private SmsService smsService;

    @Autowired
    public indexController(SmsService smsService) {
        this.smsService = smsService;
    }

    //    @GetMapping("reg")
//    public String reg(){
//        return "reg";
//    }
    @GetMapping("sendCode/{phone}")
    public R senCode(@PathVariable String phone) {
        smsService.senCode(phone);
        return R.ok();
    }

}
