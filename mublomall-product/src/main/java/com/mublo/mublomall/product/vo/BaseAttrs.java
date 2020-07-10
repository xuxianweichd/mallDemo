/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-06-05 21:38
 */
package com.mublo.mublomall.product.vo;

import lombok.Data;

@Data
public class BaseAttrs {

    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    private String attrValues;
    private int showDesc;



}