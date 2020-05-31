package com.mublo.mublomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mublo.common.utils.PageUtils;
import com.mublo.common.utils.Query;
import com.mublo.common.utils.constant.ProductConstant;
import com.mublo.mublomall.product.dao.AttrDao;
import com.mublo.mublomall.product.dao.AttrGroupDao;
import com.mublo.mublomall.product.entity.AttrAttrgroupRelationEntity;
import com.mublo.mublomall.product.entity.AttrEntity;
import com.mublo.mublomall.product.entity.AttrGroupEntity;
import com.mublo.mublomall.product.service.AttrAttrgroupRelationService;
import com.mublo.mublomall.product.service.AttrGroupService;
import com.mublo.mublomall.product.service.AttrService;
import com.mublo.mublomall.product.service.CategoryBrandRelationService;
import com.mublo.mublomall.product.vo.AttrGroupRelationVo;
import com.mublo.mublomall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    private final AttrAttrgroupRelationService attrAttrgroupRelationService;
    private final AttrDao attrDao;
    private final CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    public AttrGroupServiceImpl(AttrAttrgroupRelationService attrAttrgroupRelationService, AttrDao attrDao,CategoryBrandRelationService categoryBrandRelationService) {
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
        this.attrDao = attrDao;
        this.categoryBrandRelationService=categoryBrandRelationService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        Long catId;
        if ( params.get("catId")!=null){
            catId = Long.parseLong((String) params.get("catId"));
            if (catId > 0) {
                wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId);
            }
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(attrGroupEntityQueryWrapper -> {
                attrGroupEntityQueryWrapper.eq("attr_group_id", key).or().like("attr_group_name", key);
            });

        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long catelog_id) {
//        AttrGroupEntity attrGroupEntity = this.getById(attrGroupId);
//        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1)、当前分类下的其他分组
        List<AttrGroupEntity> group = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelog_id));
        List<Long> collect = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2)、这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds = groupId.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3)、从当前分类的所有属性中移除这些属性；
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelog_id).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = attrDao.selectPage(new Query<AttrEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
//        if (CollectionUtils.isNotEmpty(attrAttrgroupRelationEntityList)){
//            attrAttrgroupRelationEntityList
//        }
//        QueryWrapper<AttrEntity> attrEntityQueryWrapper=new QueryWrapper<AttrEntity>();
        List<AttrEntity> attrEntities=new ArrayList<>();
        if (!attrAttrgroupRelationEntityList.isEmpty()) {
//            List<AttrEntity> collect = attrAttrgroupRelationEntityList.stream().map(item -> {
//                return attrDao.selectById(item.getAttrId());
//            }).collect(Collectors.toList());
            List<Long> collect = attrAttrgroupRelationEntityList.stream().map(item -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
//            attrEntityQueryWrapper.in("attr_id",collect);
            attrEntities = attrDao.selectBatchIds(collect);
//            IPage<AttrEntity> Page =new Page<>();
//            Page= new Query<AttrEntity>().getPage(params);
//            Page.setRecords(attrEntities);
        }
//        IPage<AttrEntity> Page= attrDao.selectPage(new Query<AttrEntity>().getPage(params),attrEntityQueryWrapper);
        return attrEntities;
    }
    @Override
    public void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos) {
        List<Long> attrId = Arrays.stream(attrGroupRelationVos).map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        List<Long> attrGroupId = Arrays.stream(attrGroupRelationVos).map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        attrAttrgroupRelationService.remove(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_id",attrId).in("attr_group_id",attrGroupId));
    }

    @Override
    public void addAttrRelation(List<AttrGroupRelationVo> attrGroupRelationVoList) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList=attrGroupRelationVoList.stream().map(item->{
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item,attrAttrgroupRelationEntity);
            int a=1;
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntityList);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(item -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item, attrGroupWithAttrsVo);
            List<AttrEntity> attrEntities = this.getRelationAttr(item.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return collect;
    }


}