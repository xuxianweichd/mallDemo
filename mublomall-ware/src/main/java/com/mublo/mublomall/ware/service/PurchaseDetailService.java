/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mublo.common.utils.PageUtils;
import com.mublo.mublomall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 20:17:57
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

