/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-09 18:26
 */

package com.mublo.mublomall.thirdparty.controller;

import com.mublo.common.utils.R;
import com.mublo.common.utils.constant.AuthServerConstant;
import com.mublo.common.utils.exectpion.BizCodeEnume;
import com.mublo.common.utils.to.SmsInfoTo;
import com.mublo.mublomall.thirdparty.Component.CuccSMS;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author: mublo
 * @Date: 2020/7/9 18:26
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    //    private Demo短信发送 demo短信发送;
//    @Autowired
//    public indexController(Demo短信发送 demo短信发送) {
//        this.demo短信发送 = demo短信发送;
//    }
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    public SmsController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("/senCode")
    public R reg(@RequestBody SmsInfoTo smsInfo) {
        final String code = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + smsInfo.getPhone());
        if (StringUtils.isNotEmpty(code)){
            long senTime= Long.parseLong(code.split("_")[1]);
            //短信60秒才能发一次
            if (System.currentTimeMillis()-senTime<60000){
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+smsInfo.getPhone(),smsInfo.getCode()+"_"+System.currentTimeMillis(),smsInfo.getTime(), TimeUnit.MINUTES);
//        Demo短信发送.发送短信HttpTest("17681710805", "0000001", "['亲爱的用户', '注册操作','mublo','3']", "http://test.dev.esandcloud.com");
        CuccSMS.sendSMS(smsInfo.getPhone(), "【mublo商城】"+smsInfo.getCall()+"您正在进行"+smsInfo.getOperationType()+"，您的验证码" + smsInfo.getCode() + "，该验证码"+smsInfo.getTime()+"分钟内有效，请勿泄漏于他人！", smsInfo.getType());
        return R.ok();
    }
}
