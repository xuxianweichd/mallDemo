package com.mublo.mublomall.order.dao;

import com.mublo.mublomall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:14:35
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
