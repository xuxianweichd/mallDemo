package com.mublo.mublomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.product.entity.AttrGroupEntity;
import com.mublo.mublomall.product.vo.AttrGroupRelationVo;

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

    PageUtils getRelationAttr(Map<String, Object> params, Long attrGroupId);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos);
}
