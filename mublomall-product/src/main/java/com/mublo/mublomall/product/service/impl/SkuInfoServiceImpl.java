/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.service.impl;

import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;
import com.mublo.mublomall.product.dao.SkuInfoDao;
import com.mublo.mublomall.product.entity.SkuImagesEntity;
import com.mublo.mublomall.product.entity.SkuInfoEntity;
import com.mublo.mublomall.product.entity.SpuInfoDescEntity;
import com.mublo.mublomall.product.service.AttrGroupService;
import com.mublo.mublomall.product.service.SkuImagesService;
import com.mublo.mublomall.product.service.SkuInfoService;
import com.mublo.mublomall.product.service.SkuSaleAttrValueService;
import com.mublo.mublomall.product.service.SpuInfoDescService;
import com.mublo.mublomall.product.vo.SkuItemVo;
import com.mublo.mublomall.product.vo.SkuSaleAttrVo;
import com.mublo.mublomall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final SpuInfoDescService spuInfoDescService;
    private final AttrGroupService attrGroupService;
    private final ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    public SkuInfoServiceImpl(SkuImagesService skuImagesService, SkuSaleAttrValueService skuSaleAttrValueService, SpuInfoDescService spuInfoDescService, AttrGroupService attrGroupService, ThreadPoolExecutor threadPoolExecutor) {
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.spuInfoDescService = spuInfoDescService;
        this.attrGroupService = attrGroupService;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {

            queryWrapper.eq("catalog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");

        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);

                if (bigDecimal.compareTo(new BigDecimal("0")) == 1) {
                    queryWrapper.le("price", max);
                }
            } catch (Exception e) {

            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 查出spuId对应的所有sku信息
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> skuInfoEntities = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));

        return skuInfoEntities;
    }

    @Override
    public SkuItemVo getItemByskuId(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        final CompletableFuture<SkuInfoEntity> skuInfoFuture=CompletableFuture.supplyAsync(()->{
            //1. SKU基本信息获取，pms_sku_info
            SkuInfoEntity info = this.getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, threadPoolExecutor);
        final CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            //2.SKU的图片信息获取，pms_sku_images
            List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(skuImagesEntities);
        }, threadPoolExecutor);

        final CompletableFuture<Void> saleFuture = skuInfoFuture.thenAcceptAsync(res -> {
            //3. 获取SPU销售属性组合 pms_product_attr_value
            List<SkuSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(skuItemSaleAttrVos);
        }, threadPoolExecutor);
        final CompletableFuture<Void> spuInfoDescFuture = skuInfoFuture.thenAcceptAsync(res -> {
            //4. 获取SPU的介绍 pms_spu_info_desc
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesp(spuInfoDescEntity);
        }, threadPoolExecutor);
        final CompletableFuture<Void> spuItemAttrGroupFuture = skuInfoFuture.thenAcceptAsync(res -> {
            //5. 获取SPU的规格参数信息
            List<SpuItemAttrGroupVo> spuItemAttrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVos);
        }, threadPoolExecutor);
        CompletableFuture.allOf(skuImagesFuture,saleFuture,spuInfoDescFuture,spuItemAttrGroupFuture).get();
        return skuItemVo;
    }
}