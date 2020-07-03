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
import com.mublo.mublomall.product.vo.Attr;
import com.mublo.mublomall.product.vo.SkuItemVo;
import com.mublo.mublomall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final SpuInfoDescService spuInfoDescService;
    private final AttrGroupService attrGroupService;

    @Autowired
    public SkuInfoServiceImpl(SkuImagesService skuImagesService, SkuSaleAttrValueService skuSaleAttrValueService, SpuInfoDescService spuInfoDescService, AttrGroupService attrGroupService) {
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.spuInfoDescService = spuInfoDescService;
        this.attrGroupService = attrGroupService;
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
    public SkuItemVo getItemByskuId(Long skuId) {
        SkuItemVo skuItemVo = new SkuItemVo();

        //1. SKU基本信息获取，pms_sku_info
        SkuInfoEntity byId = this.getById(skuId);
        skuItemVo.setInfo(byId);

        //2.SKU的图片信息获取，pms_sku_images
        List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImagesBySkuId(skuId);
        skuItemVo.setImages(skuImagesEntities);

        //3. 获取SPU销售属性组合 pms_product_attr_value
        List<Attr> skuItemSaleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(byId.getSpuId());
        skuItemVo.setSaleAttr(skuItemSaleAttrVos);


        //4. 获取SPU的介绍 pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(byId.getSpuId());
        skuItemVo.setDesp(spuInfoDescEntity);


        //5. 获取SPU的规格参数信息
        List<SpuItemAttrGroupVo> spuItemAttrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(byId.getSpuId());
        skuItemVo.setGroupAttrs(spuItemAttrGroupVos);
        return skuItemVo;
    }
}