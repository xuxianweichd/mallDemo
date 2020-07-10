/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.member.feign;

import com.mublo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mublomall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/mubloTest")
    public R membercoupons();
}
