/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.product.entity.SpuInfoEntity;
import com.mublo.mublomall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:08
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);
    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);
}

