package com.mublo.mublomall.search.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Justin
 */
@Data
public class SearchParamVo {

    private Integer pageNum=1; //页码

    private String keyWord; //页面传递过来的全文匹配关键字

    private Long catalog3Id; //三级分类id

    private String sort;

    /**
     * 好多的过滤条件：
     * hasStock(是否有货) 0/1
     * 价格区间 skuPrice：1_500/_500/500_
     * 属性 attrs:2_5寸：6寸
     * brandId:1
     */
    private Integer hasStock = 1; //是否只显示有效

//    private SkuPrice skuPrice;
    private String skuPrice;
    private List<Long> brandId; //品牌id

//    private List<Attrs> attrs;
    private List<String> attrs;
//    attrId_attrValue:attrValue:attrValue:attrValue....
//    1_5.6寸
    private String _queryString;

    /**
     * 价格区间
     */
//    @Data
//    public static class SkuPrice{
//        private BigDecimal min;
//        private BigDecimal max;
//    }

    /**
     * attrId ： 规格参数id
     * attrValue ： 规格参数某个value（多选）
     */
//    @Data
//    public static class Attrs{
//        private Long attrId;
//        private List<String> attrValue;
//    }

}
