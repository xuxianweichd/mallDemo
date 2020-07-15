/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.member.dao;

import com.mublo.mublomall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:09:42
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    int getByPhone(@Param("phone") String phone);

    int getByUsername(@Param("username") String username);

    MemberEntity getMemberInfo(@Param("username") String username);
}
