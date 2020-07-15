/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.R;
import com.mublo.mublomall.member.entity.MemberEntity;
import com.mublo.mublomall.member.to.LoginTo;
import com.mublo.mublomall.member.to.RegisterTo;
import com.mublo.mublomall.member.to.TPOSMicroBlogMandateTo;

import java.util.Map;

/**
 * 会员
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:09:42
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R Register(RegisterTo user);

    R login(LoginTo loginTo);

    MemberEntity TPOSLogin(TPOSMicroBlogMandateTo tposMicroBlogMandateTo);
}

