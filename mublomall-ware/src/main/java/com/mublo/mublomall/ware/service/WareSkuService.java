package com.mublo.mublomall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.to.SkuHasStockVo;
import com.mublo.mublomall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:17:57
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

