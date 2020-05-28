package com.mublo.mublomall.member.dao;

import com.mublo.mublomall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:09:42
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
