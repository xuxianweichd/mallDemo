package com.mublo.mublomall.order.dao;

import com.mublo.mublomall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:14:35
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
