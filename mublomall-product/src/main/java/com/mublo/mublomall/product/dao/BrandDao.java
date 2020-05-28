package com.mublo.mublomall.product.dao;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.mublo.mublomall.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.List;

/**
 * 品牌
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
    @Select("SELECT * FROM pms_brand where like '%#{key}%' limit #{page},#{limit}")
    List<BrandEntity> selectByBrandList(@Param("page") Integer page, @Param("limit") Integer limit);
    @Select("SELECT * FROM pms_brand ${ew.customSqlSegment}")
    List<BrandEntity> selectBrandList(@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("SELECT COUNT(1) FROM pms_brand")
    Integer selectBrandCount();

}
