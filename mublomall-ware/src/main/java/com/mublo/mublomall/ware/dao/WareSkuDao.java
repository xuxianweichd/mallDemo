package com.mublo.mublomall.ware.dao;

import com.mublo.common.utils.to.SkuHasStockVo;
import com.mublo.mublomall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:17:57
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Long getSkuStock(Long item);

    List<SkuHasStockVo> getSkusStock(@Param("skuIds") List<Long> skuIds);
}
