package com.mublo.mublomall.product.dao;

import com.mublo.mublomall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttrIds(@Param("baseAttrsIds") List<Long> baseAttrsIds);
}
