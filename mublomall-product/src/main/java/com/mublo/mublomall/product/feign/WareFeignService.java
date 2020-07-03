package com.mublo.mublomall.product.feign;

import com.mublo.common.utils.R;
import com.mublo.common.utils.to.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mublomall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasStock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds);
}
