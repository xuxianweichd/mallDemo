/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.product.entity.AttrEntity;
import com.mublo.mublomall.product.entity.AttrGroupEntity;
import com.mublo.mublomall.product.vo.AttrGroupRelationVo;
import com.mublo.mublomall.product.vo.AttrGroupWithAttrsVo;
import com.mublo.mublomall.product.vo.SpuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long catelog_id);

    List<AttrEntity> getRelationAttr(Long attrGroupId,boolean choose);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos);

    void addAttrRelation(List<AttrGroupRelationVo> attrGroupRelationVoList);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId);
}

