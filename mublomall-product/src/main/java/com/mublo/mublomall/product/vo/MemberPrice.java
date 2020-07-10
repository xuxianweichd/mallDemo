/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-06-04 18:16
 */
package com.mublo.mublomall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}