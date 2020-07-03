package com.mublo.mublomall.search.service;


import com.mublo.mublomall.search.vo.SearchParamVo;
import com.mublo.mublomall.search.vo.SearchResultVo;

public interface MallSearchService {

    SearchResultVo search(SearchParamVo param);

}
