package com.mublo.mublomall.product.web;

import com.mublo.mublomall.product.service.SkuInfoService;
import com.mublo.mublomall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class itemController {
    private final SkuInfoService skuInfoService;
    @Autowired
    public itemController(SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;
    }

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model){
        SkuItemVo vo=skuInfoService.getItemByskuId(skuId);
        model.addAttribute("ItemVo",vo);
        return "item";
    }
}
