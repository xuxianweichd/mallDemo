/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-10 23:43
 */

package com.mublo.mublomall.auth.service;

import com.mublo.common.utils.R;
import com.mublo.common.utils.constant.AuthServerConstant;
import com.mublo.common.utils.constant.messageConstant;
import com.mublo.common.utils.constant.regExpConstant;
import com.mublo.common.utils.exectpion.BizCodeEnume;
import com.mublo.mublomall.auth.feign.MemberFeign;
import com.mublo.mublomall.auth.to.LoginTo;
import com.mublo.mublomall.auth.to.RegisterTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: mublo
 * @Date: 2020/7/10 23:43
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Service
public class UserService {
    private StringRedisTemplate stringRedisTemplate;
    private MemberFeign memberFeign;
    @Autowired
    public UserService(StringRedisTemplate stringRedisTemplate, MemberFeign memberFeign) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.memberFeign = memberFeign;
    }

    public R Login(LoginTo loginTo) {
        return memberFeign.login(loginTo);
    }

    public R Register(RegisterTo userTo) {
        if (!userTo.getPassword().equals(userTo.getAgainPassword())){
            return R.error(BizCodeEnume.VALUE_NULL_EXCEPTION.getCode(), messageConstant.unEqualPasswordMsg).put("password",messageConstant.unEqualPasswordMsg).put("againPassword",messageConstant.unEqualPasswordMsg);
        }
        if (!userTo.getPassword().matches(regExpConstant.passwordRegExp)){
            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),messageConstant.errorRegExpPasswordMsg).put("password",messageConstant.errorRegExpPasswordMsg).put("againPassword",messageConstant.errorRegExpPasswordMsg);
        }
        final String code = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userTo.getPhone());
        if (StringUtils.isBlank(code)||!code.split("_")[0].equals(userTo.getSmsCode())){
            return R.error(BizCodeEnume.VALUE_NULL_EXCEPTION.getCode(), messageConstant.errorCodeMsg).put("smsCode",messageConstant.errorCodeMsg);
        }
        stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userTo.getPhone());
        return memberFeign.register(userTo);
    }
}
