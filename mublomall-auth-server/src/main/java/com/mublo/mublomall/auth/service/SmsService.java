/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-09 19:28
 */

package com.mublo.mublomall.auth.service;

import com.mublo.common.utils.R;
import com.mublo.common.utils.constant.messageConstant;
import com.mublo.common.utils.constant.regExpConstant;
import com.mublo.common.utils.exectpion.BizCodeEnume;
import com.mublo.common.utils.to.SmsInfoTo;
import com.mublo.mublomall.auth.feign.SmsFeign;
import org.apache.commons.lang.StringUtils;
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

    public R senCode(String phone) {
        if (StringUtils.isBlank(phone)){
            return R.error(BizCodeEnume.VALUE_NULL_EXCEPTION.getCode(), messageConstant.nullPhoneMsg);
        }else if (!phone.matches(regExpConstant.phoneRegExp)){
            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),messageConstant.errorRegExpPhoneMsg);
        }
        String smscCode= UUID.randomUUID().toString().substring(0,5);
        SmsInfoTo smsInfo=new SmsInfoTo(phone,"亲爱的用户","注册操作",smscCode,5,0);
        return smsFeign.reg(smsInfo);
    }
}
