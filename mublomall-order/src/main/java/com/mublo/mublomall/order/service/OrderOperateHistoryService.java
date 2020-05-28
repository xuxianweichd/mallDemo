package com.mublo.mublomall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.order.entity.OrderOperateHistoryEntity;

import java.util.Map;

/**
 * 订单操作历史记录
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:14:35
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
