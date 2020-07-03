package com.mublo.mublomall.search.vo;

import com.mublo.common.utils.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Justin
 */
@Data
public class SearchResultVo {

//    List<SkuEsModel> products;
    List<List<SkuEsModel>> products;
//    分写信息
    private Integer pageNum; //页码
    private Long totalRows;
    private Integer totalPages;
    private List<Integer> pageNavs;

    private List<BrandVo> relevantBrands;
    private List<CatelogVo> relevantCatelogs;
    private List<AttrVo> relevantAttrs;

//    面包屑导航
    private List<NavVo> navLine = new ArrayList<>();

    @Data
    public static class NavVo{
        private String navName;
        private String navValue;
        private String link;
    }

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatelogVo{
        private Long catelogId;
        private String catelogName;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;

        private List<String> attrValues;
    }
}
