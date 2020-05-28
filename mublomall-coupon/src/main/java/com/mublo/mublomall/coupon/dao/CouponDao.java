package com.mublo.mublomall.coupon.dao;

import com.mublo.mublomall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 19:36:22
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
