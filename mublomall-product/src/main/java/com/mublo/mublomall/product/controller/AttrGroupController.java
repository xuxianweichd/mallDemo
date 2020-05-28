package com.mublo.mublomall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.mublo.mublomall.product.service.CategoryService;
import com.mublo.mublomall.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mublo.mublomall.product.entity.AttrGroupEntity;
import com.mublo.mublomall.product.service.AttrGroupService;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.R;



/**
 * 属性分组
 *
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    //构造器注入
    private final CategoryService categoryService;
    @Autowired
    public AttrGroupController(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R listTree(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path=categoryService.findCateLogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }
    /**
     * 信息
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(Map<String,Object> params ,@PathVariable("attrGroupId") Long attrGroupId){
        PageUtils page = attrGroupService.getRelationAttr(params,attrGroupId);
        return R.ok().put("page",page);
    }
    /**
     * 信息
     */
    @RequestMapping("/{catelog_id}/noattr/relation")
    public R attrNoRelation(Map<String,Object> params ,@PathVariable("catelog_id") Long catelog_id){
        PageUtils page = attrGroupService.getNoRelationAttr(params,catelog_id);
        return R.ok().put("page",page);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/relation/delete")
    public R delete(@RequestBody AttrGroupRelationVo[] attrGroupRelationVos){
//        attrService.removeByIds(Arrays.asList(attrIds));
        attrGroupService.deleteRelation(attrGroupRelationVos);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
