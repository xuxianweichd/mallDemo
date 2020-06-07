package com.mublo.mublomall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mublo.mublomall.product.config.Message;
import com.mublo.mublomall.product.valid.AddGroup;
import com.mublo.mublomall.product.valid.ListValue;
import com.mublo.mublomall.product.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author mublo
 * @email 348631648@qq.com
 * @date 2020-04-30 18:00:09
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "修改必须指定品牌id",groups = UpdateGroup.class)
	@Null(message = "新增不能指定品牌id",groups = AddGroup.class)
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "新增必须填写品牌名",groups = AddGroup.class)
//	@NotBlank(message = "品牌名填写")
//	@NotBlank.List(@NotBlank(message="品牌名必须填写"))
	private String name;
	/**
	 * 品牌logo地址
	 */
//	@Pattern(regexp = Message.logo,
//			message = Message.mes_logo)
	@NotBlank(groups = {AddGroup.class})
	@URL(message = "logo必须是一个合法的url地址",groups={AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
//	@NotBlank.List(@NotBlank(message="11"),)
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
//	@TableLogic(value = "1",delval = "0")
//	@ListValue(value={0,1},groups = AddGroup.class)
	@ListValue(value={0,1},message = "新增必须填写显示状态0或1",groups = AddGroup.class)
	private Integer showStatus;

	/**
	 * 检索首字母
	 */
	@NotEmpty(groups={AddGroup.class})
	@Pattern(regexp=Message.firstLetter,message = Message.mes_firstLetter,groups=AddGroup.class)
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 1,message ="新增排序必须填写并且大于等于0",groups = AddGroup.class)
	private Integer sort;

}
