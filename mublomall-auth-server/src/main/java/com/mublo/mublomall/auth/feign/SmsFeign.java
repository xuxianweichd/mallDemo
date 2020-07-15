/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-09 18:54
 */

package com.mublo.mublomall.auth.feign;

import com.mublo.common.utils.R;
import com.mublo.common.utils.to.SmsInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: mublo
 * @Date: 2020/7/9 18:54
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@FeignClient("mublomall-third-party")
public interface SmsFeign {
    @PostMapping("/sms/senCode")
    public R reg(@RequestBody SmsInfoTo smsInfo);
}
