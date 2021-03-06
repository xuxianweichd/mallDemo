/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.mublo.common.utils.exectpion.BizCodeEnume;
import com.mublo.mublomall.member.to.LoginTo;
import com.mublo.mublomall.member.to.RegisterTo;
import com.mublo.mublomall.member.to.TPOSMicroBlogMandateTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mublo.mublomall.member.entity.MemberEntity;
import com.mublo.mublomall.member.service.MemberService;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.R;



/**
 * 会员
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:09:42
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginTo loginTo){
        return memberService.login(loginTo);
    }
    /**
     * 第三方授权登录
     */
    @PostMapping("/TPOS/login")
    public R TPOSLogin(@RequestBody TPOSMicroBlogMandateTo tposMicroBlogMandateTo){
        MemberEntity memberEntity = memberService.TPOSLogin(tposMicroBlogMandateTo);
        if (memberEntity==null){
            return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
        }
        return R.ok().put("data",memberEntity);
    }
    /**
     * 注册
     */
    @RequestMapping("/register")
    public R register(@RequestBody RegisterTo userTo){
        return memberService.Register(userTo);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
