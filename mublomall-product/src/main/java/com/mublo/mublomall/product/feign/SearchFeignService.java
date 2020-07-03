package com.mublo.mublomall.product.feign;

import com.mublo.common.utils.R;
import com.mublo.common.utils.to.es.SkuEsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("mublomall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    public R saveProductUp(@RequestBody List<SkuEsModel> models);
}
