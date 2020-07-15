/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-11 19:29
 */

package com.mublo.mublomall.auth.service;

import com.alibaba.fastjson.JSON;
import com.mublo.common.utils.HttpUtil;
import com.mublo.common.utils.R;
import com.mublo.mublomall.auth.constant.TPOSConstant;
import com.mublo.mublomall.auth.feign.MemberFeign;
import com.mublo.mublomall.auth.to.TPOSMicroBlogMandateTo;
import com.mublo.mublomall.auth.vo.TPOSKey;
import com.mublo.mublomall.auth.vo.TPOSSecret;
import com.mublo.mublomall.auth.vo.TPOSCallBackUrl;
import com.mublo.mublomall.auth.vo.TPOSUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mublo
 * @Date: 2020/7/11 19:29
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Service
@RefreshScope
public class TPOSService {
    private MemberFeign memberFeign;
    @Autowired
    public TPOSService(MemberFeign memberFeign) {
        this.memberFeign = memberFeign;
    }

    public R getMicroBlogToken(String code) throws IOException {
        Map<String,String> params=new HashMap<>();
        params.put("client_id", TPOSKey.getMicroBlog());
        params.put("client_secret", TPOSSecret.getMicroBlog());
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", TPOSCallBackUrl.getMicroBlog());
        params.put("code", code);
        String s = HttpUtil.postForm(TPOSConstant.microBlogGetToken, params);
        TPOSMicroBlogMandateTo tposMicroBlogManDate = JSON.parseObject(s, TPOSMicroBlogMandateTo.class);
        return memberFeign.TPOSLogin(tposMicroBlogManDate);
    }

    public TPOSUrl getTPOSUrl() {
        TPOSUrl tposUrl=new TPOSUrl();
        tposUrl.setMicroBlogUrl(TPOSConstant.microBlogFront+TPOSKey.getMicroBlog()+TPOSConstant.microBlogMiddle+ TPOSCallBackUrl.getMicroBlog());
        return tposUrl;
    }
//    public TPOSKeyUrl getAppKey() {
//        TPOSKeyUrl tposKeyUrl = new TPOSKeyUrl();
//        return tposKeyUrl;
//    }
}
