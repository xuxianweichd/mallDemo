/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.search.service;


import com.mublo.mublomall.search.vo.SearchParamVo;
import com.mublo.mublomall.search.vo.SearchResultVo;

public interface MallSearchService {

    SearchResultVo search(SearchParamVo param);

}
