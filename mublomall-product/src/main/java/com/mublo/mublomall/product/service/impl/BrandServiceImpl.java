/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mublo.common.utils.Constant;
import com.mublo.mublomall.product.entity.AttrGroupEntity;
import com.mublo.mublomall.product.entity.CategoryBrandRelationEntity;
import com.mublo.mublomall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.sql.Wrapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;

import com.mublo.mublomall.product.dao.BrandDao;
import com.mublo.mublomall.product.entity.BrandEntity;
import com.mublo.mublomall.product.service.BrandService;
import sun.rmi.runtime.Log;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
private final CategoryBrandRelationService categoryBrandRelationService;
@Autowired
public BrandServiceImpl(CategoryBrandRelationService categoryBrandRelationService){
    this.categoryBrandRelationService=categoryBrandRelationService;
}
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

//    @Override
//    public PageUtils listWithTree(Map<String, Object> params) {
//        //显示条数
//        Integer pageSize= 10;
//        //显示页数
//        Integer currPage= 1;
//        if(params.get(Constant.LIMIT) != null){
//            pageSize=  Integer.parseInt((String)params.get("limit"));
//        }
//        if(params.get(Constant.PAGE) != null){
//            currPage= (Integer.parseInt((String)params.get("page"))-1)*pageSize;
//        }
//        List<BrandEntity> brandEntities=baseMapper.selectByBrandList(currPage,pageSize);
//        //总数
//        Integer totalCount= brandEntities.size();
//        return new PageUtils(brandEntities,totalCount,pageSize,currPage);
//    }
    @Override
    public PageUtils listWithTree(Map<String, Object> params) {
        //显示条数
        Integer pageSize= 10;
        //显示页数
        Integer currPage= 0;
        if(params.get(Constant.LIMIT) != null){
            pageSize=  Integer.parseInt((String)params.get("limit"));
        }
        if(params.get(Constant.PAGE) != null){
            currPage= (Integer.parseInt((String)params.get("page"))-1)*pageSize;
        }
        QueryWrapper<BrandEntity> wrapper=new QueryWrapper();
        //总数
        Integer totalCount = baseMapper.selectBrandCount();
        String key= (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("name",key).or().like("descript",key);
        }
        wrapper.last("limit "+currPage+","+pageSize);
        List<BrandEntity> brandEntities=baseMapper.selectBrandList(wrapper);
        return new PageUtils(brandEntities,totalCount,pageSize,currPage);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        BrandEntity brandEntity = this.getById(brand);
        if (brandEntity!=null){
            this.updateById(brand);
            if (!"".equals(brand.getName())&&!brand.getName().equals(brandEntity.getName())){
                UpdateWrapper<CategoryBrandRelationEntity> updateWrapper=new UpdateWrapper<>();
                updateWrapper.eq("brand_id",brand.getBrandId()).set("brand_name",brand.getName());
                categoryBrandRelationService.update(updateWrapper);
            }
        }
    }
//    @Override
//    public List<BrandEntity> listWithTree(Map<String, Object> params) {
////        QueryWrapper<BrandEntity> brandEntitie=new QueryWrapper<>();
////        brandEntitie.ge("show_status",0);
////        brandEntitie.sql
////        String sqlSelect = brandEntities.getSqlSelect("11");
////        baseMapper.
////                baseMapper.
////        baseMapper.selectByBrandList()
////        IPage<BrandEntity> page = new Query<BrandEntity>().getPage(params);
//
////        List<BrandEntity> brandEntities=brandDao.selectByBrandList(3);
//        return brandEntities;
//    }


}