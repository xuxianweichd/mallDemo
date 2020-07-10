/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mublo.common.utils.Query;
import com.mublo.mublomall.product.entity.AttrEntity;
import com.mublo.mublomall.product.service.CategoryService;
import com.mublo.mublomall.product.vo.AttrGroupRelationVo;
import com.mublo.mublomall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 查询属性分组信息并包含分类全路径
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
     * 获取分类下所有分组&关联属性
     */
    @RequestMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithAttrsVo> vos =  attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
//        AttrGroupEntity attrGroup = attrGroupService.getById(catelogId);
//        Long catelogId = attrGroup.getCatelogId();
//        Long[] path=categoryService.findCateLogPath(catelogId);
//        attrGroup.setCatelogPath(path);
        return R.ok().put("data",vos);
    }
    /**
     * 查询所有关联的基本属性
     */
    @RequestMapping("/{attrGroupId}/attr/relation/{choose}")
    public R attrRelation(@RequestParam Map<String,Object> params ,@PathVariable("attrGroupId") Long attrGroupId,@PathVariable("choose") boolean choose){
        List<AttrEntity> attrEntity = attrGroupService.getRelationAttr(attrGroupId,choose);
        IPage<AttrEntity> page=new Query<AttrEntity>().getPage(params);
        page.setRecords(attrEntity);
        return R.ok().put("page",page);
    }
    /**
     * 查询所有非关联的基本属性
     */
    @RequestMapping("/{catelog_id}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String,Object> params ,@PathVariable("catelog_id") Long catelog_id){
        PageUtils page = attrGroupService.getNoRelationAttr(params,catelog_id);
        return R.ok().put("page",page);
    }
    /**
     * 新增关联的属性
     */
    @PostMapping("/attr/relation")
    public R addAttrRelation(@RequestBody
    List<AttrGroupRelationVo> attrGroupRelationVoList){
        attrGroupService.addAttrRelation(attrGroupRelationVoList);
        return R.ok();
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
     * 批量删除关联链接（中间表）
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
