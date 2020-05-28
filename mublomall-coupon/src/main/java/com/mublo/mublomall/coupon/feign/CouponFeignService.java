package com.mublo.mublomall.coupon.feign;

import com.mublo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mublomall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/mubloTest")
    public R membercoupons();
}
