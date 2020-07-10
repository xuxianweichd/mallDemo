/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.search.controller;

import com.mublo.mublomall.search.service.MallSearchService;
import com.mublo.mublomall.search.vo.SearchParamVo;
import com.mublo.mublomall.search.vo.SearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchIndexController {

    private final MallSearchService mallSearchService;
    @Autowired
    public SearchIndexController(MallSearchService mallSearchService) {
        this.mallSearchService = mallSearchService;
    }

    @GetMapping("search.html")
    public String search(SearchParamVo param, Model model, HttpServletRequest request){
        param.set_queryString(request.getQueryString());
        SearchResultVo respVo = mallSearchService.search(param);
        model.addAttribute("result", respVo);
        return "list";
    }

}
