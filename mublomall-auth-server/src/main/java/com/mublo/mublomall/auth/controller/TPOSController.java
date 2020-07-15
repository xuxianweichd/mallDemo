/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-12 08:25
 */

package com.mublo.mublomall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.mublo.common.utils.R;
import com.mublo.common.utils.vo.OauthMember;
import com.mublo.mublomall.auth.service.TPOSService;
import com.mublo.mublomall.auth.vo.TPOSKey;
import com.mublo.mublomall.auth.vo.TPOSCallBackUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @author: mublo
 * @Date: 2020/7/12 8:25
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Controller
public class TPOSController {
    private TPOSService tposService;

    @Autowired
    public TPOSController(TPOSService tposService) {
        this.tposService = tposService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("TPOSUrl", tposService.getTPOSUrl());
        return "index";
    }

    @GetMapping("/oauth2.0/weibo/success")
    public String index(@RequestParam("code") String code, RedirectAttributes redirectAttributes, HttpSession httpSession) throws IOException {
        R r = tposService.getMicroBlogToken(code);
        if (r.getCode() == 0) {
            TypeReference<OauthMember> typeReference = new TypeReference<OauthMember>() {
            };
            OauthMember memberEntity = r.getData(typeReference);
            //a01c59ef-6e7a-4db1-94c1-7600f91e50fc
            httpSession.setAttribute("memberEntity", memberEntity);
            redirectAttributes.addFlashAttribute("member", memberEntity);
            return "redirect:http://mublomall.com";
        }
        System.out.println(r.toString());
        redirectAttributes.addFlashAttribute("errors",r);
        return "redirect:http://auth.gulimall.com/login.html";
    }
}
