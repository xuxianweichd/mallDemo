package com.mublo.mublomall.product.service.impl;

import com.mublo.mublomall.product.dao.BrandDao;
import com.mublo.mublomall.product.dao.CategoryDao;
import com.mublo.mublomall.product.entity.BrandEntity;
import com.mublo.mublomall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;

import com.mublo.mublomall.product.dao.CategoryBrandRelationDao;
import com.mublo.mublomall.product.entity.CategoryBrandRelationEntity;
import com.mublo.mublomall.product.service.CategoryBrandRelationService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
private final BrandDao brandDao;
private final CategoryDao categoryDao;
@Autowired
public CategoryBrandRelationServiceImpl(BrandDao brandDao,CategoryDao categoryDao){
    this.brandDao=brandDao;
    this.categoryDao=categoryDao;
}


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brand_id=categoryBrandRelation.getBrandId();
        Long catelog_id=categoryBrandRelation.getCatelogId();
        Map<String,Object> map=new HashMap<>();
        map.put("brand_id",brand_id);
        map.put("catelog_id",catelog_id);
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = baseMapper.selectByMap(map);
        if (categoryBrandRelationEntities.size()>0){
            throw new RuntimeException("不能重复关联");
        }
        BrandEntity brandEntity = brandDao.selectById(brand_id);
        CategoryEntity categoryEntity = categoryDao.selectById(catelog_id);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId,name);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {

        List<CategoryBrandRelationEntity> catelogId = this.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> collect = catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            BrandEntity byId = brandDao.selectById(brandId);
            return byId;
        }).collect(Collectors.toList());
        return collect;
    }

}