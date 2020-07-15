/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 18:41
 */

package com.mublo.mublomall.auth.controller;

import com.mublo.common.utils.R;
import com.mublo.common.utils.exectpion.BizCodeEnume;
import com.mublo.mublomall.auth.service.SmsService;
import com.mublo.mublomall.auth.service.UserService;
import com.mublo.mublomall.auth.to.LoginTo;
import com.mublo.mublomall.auth.to.RegisterTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: mublo
 * @Date: 2020/7/7 18:41
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Controller
//@RefreshScope
public class indexController {
    private SmsService smsService;
    private UserService userService;
    @Autowired
    public indexController(SmsService smsService, UserService userService) {
        this.smsService = smsService;
        this.userService = userService;
    }



    @ResponseBody
    @GetMapping("/sendCode/{phone}")
    public R senCode(@PathVariable String phone) {
        return smsService.senCode(phone);
    }

    @PostMapping("/login")
    public String login(@Valid LoginTo loginTo, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            final Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            System.out.println(errors.toString());
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.mublomall.com/index";
//            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg());
        }
        R r = userService.Login(loginTo);
        if (r.get("code").equals(BizCodeEnume.LOGIN_EXCEPTION.getCode())){
            System.out.println(r.toString());
            redirectAttributes.addFlashAttribute("errors",r);
            return "redirect:http://auth.mublomall.com/index";
//            return R.error(BizCodeEnume.LOGIN_EXCEPTION.getCode(),BizCodeEnume.LOGIN_EXCEPTION.getMsg());
        }
        return "redirect:http://mublomall.com";
    }

    /**
     *  //TODO 重定向携带数据，利用session原理。将数据放在session中。
     *      只要跳到下一个页面去处这个数据以后，session里面的数据就会删掉
     *  //TODO 1、分布式下的session问题
     *  RedirectAttributes 模拟重定向携带数据
     * @param userTo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid RegisterTo userTo, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            final Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.mublomall.com/reg";
//            servletResponse.sendRedirect("http://auth.mublomall.com/reg");
        }
        R r = userService.Register(userTo);
        if (!r.get("code").equals(0)){
            redirectAttributes.addFlashAttribute("errors",r);
            return "redirect:http://auth.mublomall.com/reg";
        }
        return "index";
//        return r;
    }

}
