package com.mublo.mublomall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.mublo.mublomall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mublo.mublomall.product.entity.AttrEntity;
import com.mublo.mublomall.product.service.AttrService;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.R;



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
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);
int i=9;
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
		AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
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
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

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
