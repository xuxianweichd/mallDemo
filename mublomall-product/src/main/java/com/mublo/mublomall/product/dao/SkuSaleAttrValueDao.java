package com.mublo.mublomall.product.dao;

import com.mublo.mublomall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mublo.mublomall.product.vo.Attr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<Attr> getSaleAttrsBySpuId(@Param("spuId") Long spuId);
}
