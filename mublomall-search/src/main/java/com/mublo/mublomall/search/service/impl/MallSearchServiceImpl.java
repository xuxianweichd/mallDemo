/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.mublo.common.utils.R;
import com.mublo.common.utils.to.es.SkuEsModel;
import com.mublo.mublomall.search.config.MublomallElasticSearchConfig;
import com.mublo.mublomall.search.constant.EsConstant;
import com.mublo.mublomall.search.feign.AttrFeignService;
import com.mublo.mublomall.search.service.MallSearchService;
import com.mublo.mublomall.search.vo.SearchParamVo;
import com.mublo.mublomall.search.vo.SearchResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Justin
 */
@Slf4j
@Service("mallSearchService")
public class MallSearchServiceImpl implements MallSearchService {
    private final RestHighLevelClient esClient;
    private final AttrFeignService attrFeignService;

    @Autowired
    public MallSearchServiceImpl(RestHighLevelClient esClient, AttrFeignService attrFeignService) {
        this.esClient = esClient;
        this.attrFeignService = attrFeignService;
    }

    @Override
    public SearchResultVo search(SearchParamVo param) {
        SearchResultVo searchResultVo = null;
        SearchRequest searchRequest = buildSearchRequest(param);

        try {
            SearchResponse searchResponse = esClient.search(searchRequest, MublomallElasticSearchConfig.COMMON_OPTIONS);
//           分析响应数据
            searchResultVo = buildSearchResult(param, searchResponse, param.getAttrs());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResultVo;
    }

    /**
     * @param list 要拆分的集合
     * @param n    每n个拆分成一个新集合
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> list, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
//        查询到的集合小于拆分量直接返回
        if (list.size() < n) {
            result.add(list);
            return result;
        }
        int remaider = list.size() % n;  //(先计算出余数)
        int number = remaider > 0 ? list.size() / n + 1 : (list.size() / n); //能被切割成几份
        int offset = 0;//偏移量
        for (int i = 0; i < number; i++) {
            List<T> value = null;
            if (i == number - 1 && remaider > 0) {
                value = list.subList(offset, offset + remaider);
                offset += n;
            } else {
                value = list.subList(offset, offset + n);
                offset += n;
            }
            result.add(value);
        }
        return result;
    }

    //    public static <T> List<List<T>> averageAssign(List<T> list,int n){
//        List<List<T>> result=new ArrayList<List<T>>();
//        int remaider=list.size()%n;  //(先计算出余数)
//        int number=list.size()/n;  //然后是商（n就是需要展示的行数）
//        int offset=0;//偏移量
//        for(int i=0;i<n;i++){
//            List<T> value=null;
//            if(remaider>0){
//                value=list.subList(i*number+offset, (i+1)*number+offset+1);
//                remaider--;
//                offset++;
//            }else{
//                value=list.subList(i*number+offset, (i+1)*number+offset);
//            }
//            result.add(value);
//        }
//        return result;
//    }
    private SearchResultVo buildSearchResult(SearchParamVo param, SearchResponse response, List<String> attrs) {
        SearchResultVo result = new SearchResultVo();
        SearchHits hits = response.getHits();

        SearchHit[] subHits = hits.getHits();
        List<SkuEsModel> skuEsModels = null;
        if (subHits != null && subHits.length > 0) {
            skuEsModels = Arrays.asList(subHits).stream().map(subHit -> {
                String sourceAsString = subHit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(param.getKeyWord())) {
                    HighlightField skuTitle = subHit.getHighlightFields().get("skuTitle");
                    String skuTitleHighLight = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(skuTitleHighLight);
                }
                return skuEsModel;
            }).collect(Collectors.toList());

        }
        if (skuEsModels != null && skuEsModels.size() > 0) {
//            skuEsModels=skuEsModels.stream().filter(skuEsModel -> {
//                return skuEsModel.getAttrs()==null;
//            }).collect(Collectors.toList());
            List<List<SkuEsModel>> lists = averageAssign(skuEsModels, 4);
            //1.返回所查询到的所有商品
            result.setProducts(lists);


            //2.当前所有商品所涉及到的所有属性信息
            ParsedNested attr_agg = response.getAggregations().get("attr_agg");
            ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
            List<SearchResultVo.AttrVo> attrVos = attr_id_agg.getBuckets().stream().map(item -> {
                SearchResultVo.AttrVo attrVo = new SearchResultVo.AttrVo();

                //1.获取属性的id
                long attrId = item.getKeyAsNumber().longValue();

                //2.获取属性名
                String attrName = ((ParsedStringTerms) item.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();

                //3.获取属性的所有值
                List<String> attrValues = ((ParsedStringTerms) item.getAggregations().get("attr_value_agg")).getBuckets().stream().map(bucket -> {
                    return bucket.getKeyAsString();
                }).collect(Collectors.toList());
                if (attrs != null && attrs.size() > 0){
                    Iterator<String> iterator = attrValues.iterator();
                    while (iterator.hasNext() ) {
                        String next = iterator.next();
                        for (String attr : attrs) {
                            String[] s = attr.split("_");
                            String[] attrvalues = s[1].split(":");
                            for (String attrvalue : attrvalues) {
                                if (attrvalue.equals(next)) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                    iterator.forEachRemaining(attrValues::add);
                }
                if (attrValues != null && attrValues.size() > 0) {
                    attrVo.setAttrValues(attrValues);
                }
                attrVo.setAttrId(attrId);
                attrVo.setAttrName(attrName);
                return attrVo;
            }).collect(Collectors.toList());

            result.setRelevantAttrs(attrVos);

            //3.当前所有商品所涉及到的所有品牌信息
            ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
            List<SearchResultVo.BrandVo> brandVos = brand_agg.getBuckets().stream().map(item -> {
                SearchResultVo.BrandVo brandVo = new SearchResultVo.BrandVo();
                //1.获取id
                long brandId = item.getKeyAsNumber().longValue();
                //2.获取品牌名
                String brandName = ((ParsedStringTerms) item.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();

                //3.获取品牌图片
                String brandImag = ((ParsedStringTerms) item.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();

                brandVo.setBrandId(brandId);
                brandVo.setBrandName(brandName);
                brandVo.setBrandImg(brandImag);
                return brandVo;
            }).collect(Collectors.toList());

            result.setRelevantBrands(brandVos);

            //4.当前所有商品所涉及到的所有分类信息
            ParsedLongTerms catelog_agg = response.getAggregations().get("catelog_agg");
            List<SearchResultVo.CatelogVo> catelogVos = catelog_agg.getBuckets().stream().map(item -> {
                SearchResultVo.CatelogVo catelogVo = new SearchResultVo.CatelogVo();
                //获取分类ID
                String catelogId = item.getKeyAsString();
                catelogVo.setCatelogId(Long.parseLong(catelogId));

                //获取分类名
                ParsedStringTerms catelog_name_agg = item.getAggregations().get("catelog_name_agg");
                String catelogName = catelog_name_agg.getBuckets().get(0).getKeyAsString();
                catelogVo.setCatelogName(catelogName);
                return catelogVo;
            }).collect(Collectors.toList());

            result.setRelevantCatelogs(catelogVos);

            //=========以上从聚合信息中获取===========
            //5.分页信息-当前页码
            result.setPageNum(param.getPageNum());
            //5.分页信息-总记录数
            long total = hits.getTotalHits().value;
            result.setTotalRows(total);
            //5.分页信息-总页码
            boolean flag = total % EsConstant.PRODUCT_PAGESIZE == 0;
            int totalPage = flag ? (int) total / EsConstant.PRODUCT_PAGESIZE : ((int) total / EsConstant.PRODUCT_PAGESIZE) + 1;
            result.setTotalPages(totalPage);

            ArrayList<Integer> page = new ArrayList<>();
            for (int i = 1; i <= totalPage; i++) {
                page.add(i);
            }
            result.setPageNavs(page);
            //构建面包屑导航属性vo
            if (param.getAttrs() != null && param.getAttrs().size() > 0) {
                List<SearchResultVo.NavVo> navVos = param.getAttrs().stream().map(attr -> {
                    SearchResultVo.NavVo navVo = new SearchResultVo.NavVo();
                    String[] Attr = attr.split("_");
                    navVo.setNavValue(Attr[1]);
                    List<SearchResultVo.AttrVo> s = result.getRelevantAttrs().stream().filter(attrVo -> {
//                        String a= String.valueOf(attrVo.getAttrId());\

                        return Long.valueOf(Attr[0]) == attrVo.getAttrId();
                    }).collect(Collectors.toList());
                    navVo.setNavName(s.get(0).getAttrName());
//                    远程调用获取attr名字
//                    R r = attrFeignService.attrInfo(Long.parseLong(Attr[0]));
//                    if (r.getCode() == 0) {
//                        String entityValue = (String) r.getEntityValue("attr", "attrName");
////                        Object data = r.get("attr");
////                        String s1 = JSON.toJSONString(data);
////                        AttrRespVo attrRespVo = JSON.parseObject(s1, AttrRespVo.class);
//                        navVo.setNavName(entityValue);
//                    } else {
//                        navVo.setNavName(Attr[0]);
//                    }
                    String replace = replaceParamString(param.get_queryString(), attr, "&attrs");
                    param.set_queryString(replace);
                    navVo.setLink("http://search.mublomall.com/search.html?" + replace);
//                    navVo.setLink("http://localhost:13000/list.html?" + replace);
                    return navVo;
                }).collect(Collectors.toList());
                result.setNavLine(navVos);
            }
        }
        return result;
    }

    private String replaceParamString(String s, String value, String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            encode = encode.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s.replace(key + "=" + encode, "");
    }

    private SearchRequest buildSearchRequest(SearchParamVo param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(param.getKeyWord())) {
//            模糊查询商品标题
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyWord()));
        }

        if (param.getCatalog3Id() != null) {
//            过滤器
            boolQuery.filter(QueryBuilders.termQuery("catelogId", param.getCatalog3Id()));
        }

        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }

        boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        //TODO 字符串拼接拆分的方式es查询价格范围 1_500/ _500/ 500_
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[0]);
                } else {
                    rangeQuery.gte(s[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            param.getAttrs().forEach(attrs -> {
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                String[] s = attrs.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                //检索的规格id
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                //检索的规格值集合
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                //每一个都必须生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            });
        }
//        if (param.getSkuPrice() != null) {
//            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
//            if (param.getSkuPrice().getMax() != null) {
////                小于等于价格最大范围
//                rangeQuery.lte(param.getSkuPrice().getMax());
//            }
//            if (param.getSkuPrice().getMin() != null) {
////                大于等于价格的最小范围
//                rangeQuery.gte(param.getSkuPrice().getMax());
//            }
//            boolQuery.filter(rangeQuery);
//        }
//        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
//            param.getAttrs().forEach(attrs -> {
//                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
//                //检索的规格id
//                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrs.getAttrId()));
//                //检索的规格值集合
//                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrs.getAttrValue()));
//                //每一个都必须生成一个nested查询
//                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
//                boolQuery.filter(nestedQuery);
//            });
//        }
        sourceBuilder.query(boolQuery);

//        排序
        if (!StringUtils.isEmpty(param.getSort())) {
            String sort = param.getSort();
            String[] s = sort.split("_");
            SortOrder sortOrder = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], sortOrder);
        }

//        分页
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        //高亮
        if (!StringUtils.isEmpty(param.getKeyWord())) {

            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");

            sourceBuilder.highlighter(highlightBuilder);
        }
//        聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(20);
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        TermsAggregationBuilder catelog_agg = AggregationBuilders.terms("catelog_agg").field("catelogId").size(2);
        catelog_agg.subAggregation(AggregationBuilders.terms("catelog_name_agg").field("catelogName").size(1));
        sourceBuilder.aggregation(catelog_agg);

        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(50);
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);

        System.out.println("DSL: " + sourceBuilder.toString());

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);

        return searchRequest;
    }

}
