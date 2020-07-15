/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mublo.common.utils.HttpUtil;
import com.mublo.common.utils.R;
import com.mublo.common.utils.constant.messageConstant;
import com.mublo.common.utils.exectpion.BizCodeEnume;
import com.mublo.mublomall.member.dao.MemberLevelDao;
import com.mublo.mublomall.member.to.LoginTo;
import com.mublo.mublomall.member.to.RegisterTo;
import com.mublo.mublomall.member.to.TPOSMicroBlogMandateTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;

import com.mublo.mublomall.member.dao.MemberDao;
import com.mublo.mublomall.member.entity.MemberEntity;
import com.mublo.mublomall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    private MemberLevelDao memberLevelDao;

    @Autowired
    public MemberServiceImpl(MemberLevelDao memberLevelDao) {
        this.memberLevelDao = memberLevelDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R Register(RegisterTo registerTo) {
        final int phoneCount = baseMapper.getByPhone(registerTo.getPhone());
        if (phoneCount == 1) {
            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), messageConstant.existPhoneMsg).put("phone", messageConstant.existPhoneMsg);
        }
        final int usernameCount = baseMapper.getByUsername(registerTo.getUsername());
        if (usernameCount == 1) {
            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), messageConstant.existUserMsg).put("username", messageConstant.existUserMsg);
        }
        MemberEntity memberEntity = new MemberEntity();
        Long level = memberLevelDao.getDefaultLevel(1);
        memberEntity.setLevelId(level);
        memberEntity.setCreateTime(new Date());
        memberEntity.setUsername(registerTo.getUsername());
        memberEntity.setMobile(registerTo.getPhone());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(registerTo.getPassword());
        memberEntity.setPassword(encode);
        baseMapper.insert(memberEntity);
        return R.ok();
    }

    @Override
    public R login(LoginTo loginTo) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        MemberEntity memberEntity = baseMapper.getMemberInfo(loginTo.getUsername());
        if (memberEntity == null) {
            return R.error(BizCodeEnume.LOGIN_EXCEPTION.getCode(), BizCodeEnume.LOGIN_EXCEPTION.getMsg());
        }
        final boolean matches = bCryptPasswordEncoder.matches(loginTo.getPassword(), memberEntity.getPassword());
        if (!matches) {
            return R.error(BizCodeEnume.LOGIN_EXCEPTION.getCode(), BizCodeEnume.LOGIN_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @Override
    public MemberEntity TPOSLogin(TPOSMicroBlogMandateTo tposMicroBlogMandateTo) {
        Long uid = tposMicroBlogMandateTo.getUid();
        MemberEntity memberEntity = this.getOne(new QueryWrapper<MemberEntity>().eq("uid", uid));
        if (memberEntity != null) {
//            已存在账户
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setExpires_in(tposMicroBlogMandateTo.getExpires_in());
            update.setAccess_token(tposMicroBlogMandateTo.getAccess_token());
            this.updateById(update);

            memberEntity.setExpires_in(tposMicroBlogMandateTo.getExpires_in());
            memberEntity.setAccess_token(tposMicroBlogMandateTo.getAccess_token());

            return memberEntity;

        }
//            新建账户
        MemberEntity newMember = new MemberEntity();
        newMember.setAccess_token(tposMicroBlogMandateTo.getAccess_token());
        newMember.setExpires_in(tposMicroBlogMandateTo.getExpires_in());
        newMember.setUid(tposMicroBlogMandateTo.getUid());

//https://api.weibo.com/2/users/show.json?uid=1913287700&access_token=2.00CXxTFCVBijpDf1d794d47b0YN137
        Map<String, String> map = new HashMap<>();
        map.put("access_token", tposMicroBlogMandateTo.getAccess_token());
        map.put("uid", tposMicroBlogMandateTo.getUid().toString());
        String result = "";
        try {
            String url = HttpUtil.appendQueryParams("https://api.weibo.com/2/users/show.json", map);
            result = HttpUtil.get(url);
        } catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(result);
        String name = (String) jsonObject.get("name");
        newMember.setNickname(name);

        this.baseMapper.insert(newMember);
        return newMember;
    }
}