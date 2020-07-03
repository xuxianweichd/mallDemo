package com.mublo.mublomall.ware.service.impl;

import com.mublo.common.utils.to.SkuHasStockVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;

import com.mublo.mublomall.ware.dao.WareSkuDao;
import com.mublo.mublomall.ware.entity.WareSkuEntity;
import com.mublo.mublomall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
//        List<SkuHasStockVo> skuHasStockVos = skuIds.stream().map(item -> {
//            //查询当前sku的总库存
//            Long count = baseMapper.getSkuStock(item);
//            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
//            skuHasStockVo.setSkuId(item);
//            skuHasStockVo.setHasStock(count == null?false:count > 0);
//            return skuHasStockVo;
//        }).collect(Collectors.toList());
        List<SkuHasStockVo> skuHasStockVos = baseMapper.getSkusStock(skuIds);
        return skuHasStockVos;
    }

}