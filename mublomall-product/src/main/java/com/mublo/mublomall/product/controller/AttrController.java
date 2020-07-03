package com.mublo.mublomall.product.controller;

import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.R;
import com.mublo.mublomall.product.entity.ProductAttrValueEntity;
import com.mublo.mublomall.product.service.AttrService;
import com.mublo.mublomall.product.service.ProductAttrValueService;
import com.mublo.mublomall.product.vo.AttrRespVo;
import com.mublo.mublomall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



/**
 * 商品属性
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;
    @Autowired
    public AttrController(AttrService attrService, ProductAttrValueService productAttrValueService) {
        this.attrService = attrService;
        this.productAttrValueService = productAttrValueService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }
    @GetMapping("/{attrType}/list/{catId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catId") Long catId,
                          @PathVariable("attrType")String type){
        PageUtils page = attrService.queryBaseAttrPage(params,catId,type);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo respVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", respVo);
    }

    /**
     * 获取spu规格
     * @param spuId
     * @return
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrlistforspu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> respVo= productAttrValueService.getAttrValueListBySpuId(spuId);
        return R.ok().put("data", respVo);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo){
//		attrService.save(attr);
        attrService.saveAttr(attrVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody AttrVo attrVo){
//		attrService.updateById(attr);
        attrService.updateAttr(attrVo);
        return R.ok();
    }
    /**
     * 修改
     */
    @PostMapping("/update/{spuId}")
    public R update(@PathVariable("spuId") Long spuId,@RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
//		attrService.removeByIds(Arrays.asList(attrIds));
        attrService.removeRelationByids(attrIds);
        return R.ok();
    }

}
