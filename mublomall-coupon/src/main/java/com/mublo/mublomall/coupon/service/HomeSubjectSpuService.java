/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 19:36:21
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

