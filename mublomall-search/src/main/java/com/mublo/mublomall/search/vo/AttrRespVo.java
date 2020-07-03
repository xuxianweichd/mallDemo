package com.mublo.mublomall.search.vo;

import lombok.Data;

@Data
public class AttrRespVo extends SearchResultVo.AttrVo {

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;

    private Long attrGroupId;
}
