package com.mublo.mublomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.product.entity.AttrEntity;
import com.mublo.mublomall.product.vo.AttrRespVo;
import com.mublo.mublomall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    void saveAttr(AttrVo attrVo);

    void removeRelationByids(Long[] attrIds);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);
}

