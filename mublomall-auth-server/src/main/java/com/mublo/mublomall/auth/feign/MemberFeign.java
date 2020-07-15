/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-11 00:16
 */

package com.mublo.mublomall.auth.feign;

import com.mublo.common.utils.R;
import com.mublo.mublomall.auth.to.LoginTo;
import com.mublo.mublomall.auth.to.RegisterTo;
import com.mublo.mublomall.auth.to.TPOSMicroBlogMandateTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: mublo
 * @Date: 2020/7/11 0:16
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@FeignClient("mublomall-member")
public interface MemberFeign {
    /**
     * 注册
     * @param userTo
     * @return
     */
    @RequestMapping("/member/member/register")
    public R register(@RequestBody RegisterTo userTo);

    /**
     * 登录
     * @param loginTo
     * @return
     */
    @PostMapping("/member/member/login")
    public R login(@RequestBody LoginTo loginTo);
    /**
     * 第三方授权登录
     */
    @PostMapping("/member/member/TPOS/login")
    public R TPOSLogin(@RequestBody TPOSMicroBlogMandateTo tposMicroBlogMandateTo);
}
