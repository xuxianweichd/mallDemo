/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;
import com.mublo.common.utils.R;
import com.mublo.common.utils.constant.ProductConstant;
import com.mublo.common.utils.to.SkuHasStockVo;
import com.mublo.common.utils.to.SkuReductionTo;
import com.mublo.common.utils.to.SpuBoundTo;
import com.mublo.common.utils.to.es.SkuEsModel;
import com.mublo.mublomall.product.dao.SpuInfoDao;
import com.mublo.mublomall.product.entity.BrandEntity;
import com.mublo.mublomall.product.entity.CategoryEntity;
import com.mublo.mublomall.product.entity.ProductAttrValueEntity;
import com.mublo.mublomall.product.entity.SkuImagesEntity;
import com.mublo.mublomall.product.entity.SkuInfoEntity;
import com.mublo.mublomall.product.entity.SkuSaleAttrValueEntity;
import com.mublo.mublomall.product.entity.SpuImagesEntity;
import com.mublo.mublomall.product.entity.SpuInfoDescEntity;
import com.mublo.mublomall.product.entity.SpuInfoEntity;
import com.mublo.mublomall.product.feign.CouponFeignService;
import com.mublo.mublomall.product.feign.SearchFeignService;
import com.mublo.mublomall.product.feign.WareFeignService;
import com.mublo.mublomall.product.service.AttrService;
import com.mublo.mublomall.product.service.BrandService;
import com.mublo.mublomall.product.service.CategoryService;
import com.mublo.mublomall.product.service.ProductAttrValueService;
import com.mublo.mublomall.product.service.SkuImagesService;
import com.mublo.mublomall.product.service.SkuInfoService;
import com.mublo.mublomall.product.service.SkuSaleAttrValueService;
import com.mublo.mublomall.product.service.SpuImagesService;
import com.mublo.mublomall.product.service.SpuInfoDescService;
import com.mublo.mublomall.product.service.SpuInfoService;
import com.mublo.mublomall.product.vo.Attr;
import com.mublo.mublomall.product.vo.BaseAttrs;
import com.mublo.mublomall.product.vo.Bounds;
import com.mublo.mublomall.product.vo.Images;
import com.mublo.mublomall.product.vo.Skus;
import com.mublo.mublomall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    private final SpuInfoDescService spuInfoDescService;
    private final SpuImagesService spuImagesService;
    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;
    private final CouponFeignService couponFeignService;
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final WareFeignService wareFeignService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final SearchFeignService searchFeignService;

    @Autowired
    public SpuInfoServiceImpl(SpuInfoDescService spuInfoDescService, SpuImagesService spuImagesService, AttrService attrService, ProductAttrValueService productAttrValueService, CouponFeignService couponFeignService, SkuInfoService skuInfoService, SkuImagesService skuImagesService, SkuSaleAttrValueService skuSaleAttrValueService, WareFeignService wareFeignService, BrandService brandService, CategoryService categoryService, SearchFeignService searchFeignService) {
        this.spuInfoDescService = spuInfoDescService;
        this.spuImagesService = spuImagesService;
        this.attrService = attrService;
        this.productAttrValueService = productAttrValueService;
        this.couponFeignService = couponFeignService;
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.wareFeignService = wareFeignService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.searchFeignService = searchFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
//1、保存spu基本信息 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);
        //2、保存Spu的描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",", decript));
//        StringBuilder stringBuilder=new StringBuilder();
//        stringBuilder.append(",");
        spuInfoDescService.save(descEntity);
//        spuInfoDescService.saveSpuInfoDesc(descEntity);


        //3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
//        spuImagesService.saveImages(infoEntity.getId(),images);
        List<SpuImagesEntity> spuImagesEntityList = images.stream().map(item -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(infoEntity.getId());
            spuImagesEntity.setImgUrl(item);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(spuImagesEntityList);

        //4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
//            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(attr.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
//        productAttrValueService.saveProductAttr(collect);
        productAttrValueService.saveBatch(collect);


        //5、保存spu的积分信息；gulimall_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }


        //5、保存当前spu对应的所有sku信息；

        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                //    private String skuName;
                //    private BigDecimal price;
                //    private String skuTitle;
                //    private String skuSubtitle;
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //5.1）、sku的基本信息；pms_sku_info
//                skuInfoService.saveSkuInfo(skuInfoEntity);
                skuInfoService.save(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //5.2）、sku的图片信息；pms_sku_image
                skuImagesService.saveBatch(imagesEntities);

                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                //5.3）、sku的销售属性信息：pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // //5.4）、sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }


            });
        }


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        /**
         * status: 2
         * key:
         * brandId: 9
         * catelogId: 225
         */

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void up(Long spuId) {
        //查询出supId所对应的SKU信息，品牌的名字
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);

        //TODO 4. 查询当前SKU的所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.getAttrValueListBySpuId(spuId);
        List<Long> attrIds = productAttrValueEntities.stream().map(item -> {
            Long attrId = item.getAttrId();
            return attrId;
        }).collect(Collectors.toList());

        //获取到支持检索的属性的属性ID
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        HashSet<Long> searchAttrIdsSet = new HashSet<>(searchAttrIds);
        //取得支持检索的的Attrs，用来封装SkuEsModel的attrs属性
        List<SkuEsModel.Attrs> attrsList = productAttrValueEntities.stream().filter(item -> {
            return searchAttrIdsSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            attrs.setAttrId(item.getAttrId());
            attrs.setAttrName(item.getAttrName());
            attrs.setAttrValue(item.getAttrValue());
            return attrs;
        }).collect(Collectors.toList());

        //取得sku所对应的库存信息，即是否还有库存，为封装SkuEsModel的HasStock属性服务
//        Map<Long, Boolean> stockMap = null;
//        try {
//            List<Long> skuInfoSkuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
//            List<SkuHasStockVo> skuHasStockVoList=wareFeignService.getSkuHasStock(skuInfoSkuIds);
//            R skuHasStock = wareFeignService.getSkuHasStock(skuInfoSkuIds);
//
//            stockMap = skuHasStock.getData(new TypeReference<List<SkuHasStockVo>>(){}).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
//        } catch (Exception e) {
//            log.error("库存服务查询异常：原因{}",e);
//        }
        //TODo 1.发送远程调用，查询库存系统是否有库存
        Map<Long, Boolean> stockMap = null;
        try {
            R r = wareFeignService.getSkuHasStock(searchAttrIds);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            stockMap = r.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
//            stockMap = haveStockR.stream().collect(Collectors.toMap(SkuStockTo::getSkuId, SkuStockTo::getHasStock));
        } catch (Exception e) {
            log.error("库存远程调用错误");
        }
        //封装每个SKU的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> collect = skuInfoEntities.stream().map(item -> {
            //组装需要的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(item, skuEsModel);
            //skuImg skuPrice
//            skuEsModel.setSkuPrice(item.getPrice());
//            skuEsModel.setSkuImg(item.getSkuDefaultImg());

            //hasStock
            if (finalStockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(item.getSkuId()));
            }

            //hotScore
            //TODO 2. 热度评分，默认0
            skuEsModel.setHotScore(0L);

            //brandImg brandName   catelogName
            //TODO 3. 查询品牌和分类的名字信息
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());

            //catalog和catelog命名不规范的坑
            skuEsModel.setCatelogId(item.getCatalogId());
            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatelogId());
            skuEsModel.setCatelogName(categoryEntity.getName());

            //设置检索属性，attrs
            skuEsModel.setAttrs(attrsList);


            return skuEsModel;

        }).collect(Collectors.toList());

        //TODO 5. 将数据发送给ES进行保存；gulimall-search
        R statusUp = searchFeignService.saveProductUp(collect);
        if (statusUp.getCode() == 0) {
            //远程调用成功
            //TODO 6.修改当前的SPU状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            //远程调用失败
            //TODO 7.重复调用的问题，接口幂等性
            //Feign调用流程 会进去SynchronousMethodHandler.class
            /**
             * 1、构造请求数据，将对象转为json
             *      RequestTemplate template = this.buildTemplateFromArgs.create(argv); 请求模版
             * 2、发送请求金星执行（执行成功会解码响应数据）
             *      executeAndDecode(template, options);
             * 3、执行请求会有重试机制
             *      while(true) {
             *             try {
             *                 return this.executeAndDecode(template, options); 解码响应数据
             *             } catch () {
             *                 RetryableException e = var9;
             *                 try {
             *                     retryer.continueOrPropagate(e);      重试器重试
             *                 } catch (RetryableException var8) {
             *                     Throwable cause = var8.getCause();
             *                     if (this.propagationPolicy == ExceptionPropagationPolicy.UNWRAP && cause != null) {
             *                         throw cause;
             *                     }
             *
             *                     throw var8;
             *                 }
             *
             *                 if (this.logLevel != Level.NONE) { 如果有logins日志打印logs日志
             *                     this.logger.logRetry(this.metadata.configKey(), this.logLevel);
             *                 }
             *             }
             *         }
             */


        }
    }


}