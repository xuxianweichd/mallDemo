package com.mublo.mublomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;
import com.mublo.mublomall.product.dao.CategoryDao;
import com.mublo.mublomall.product.entity.CategoryBrandRelationEntity;
import com.mublo.mublomall.product.entity.CategoryEntity;
import com.mublo.mublomall.product.service.CategoryBrandRelationService;
import com.mublo.mublomall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
private final CategoryBrandRelationService categoryBrandRelationService;
@Autowired
public CategoryServiceImpl(CategoryBrandRelationService categoryBrandRelationService){
    this.categoryBrandRelationService=categoryBrandRelationService;
}
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

//    @Override
//    public List<CategoryEntity> listWithTree() {
//        QueryWrapper<CategoryEntity> categoryEntityQueryWrapper=new QueryWrapper<>();
//        categoryEntityQueryWrapper.eq("parent_cid",0);
//        List<CategoryEntity> categoryEntities = baseMapper.selectList(categoryEntityQueryWrapper);
//        return categoryEntities;
//    }
    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid().equals(0L)
        ).map((menu)->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
//            return menu1.getSort()-menu2.getSort();
        }).collect(Collectors.toList());




        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCateLogPath(Long catelogId) {
        List<Long> path=new ArrayList<>();
        CategoryEntity categoryEntity = baseMapper.selectById(catelogId);
        path.add(catelogId);
        while (categoryEntity.getParentCid()!=0){
            categoryEntity = baseMapper.selectById(categoryEntity.getParentCid());
            path.add(categoryEntity.getCatId());
        }
        Collections.reverse(path);
        return path.toArray(new Long[path.size()]);
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        CategoryEntity categoryEntity = this.getById(category.getCatId());
        if (categoryEntity!=null){
            this.updateById(category);
            if (!"".equals(category.getName())&&!categoryEntity.getName().equals(category.getName())){
                UpdateWrapper<CategoryBrandRelationEntity> updateWrapper=new UpdateWrapper<>();
                updateWrapper.eq("catelog_id",category.getCatId()).set("catelog_name",category.getName());
                categoryBrandRelationService.update(updateWrapper);
//                categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
            }
        }

    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId()) ;
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            //2、菜单的排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
//            return menu1.getSort()-menu2.getSort();
        }).collect(Collectors.toList());

        return children;
    }
}