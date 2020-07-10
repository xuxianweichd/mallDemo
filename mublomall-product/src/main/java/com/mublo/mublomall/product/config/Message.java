/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.config;

public interface Message {
//    final String name= "[`~!@#$%^&*()_+<>?:\"{},.\\/;'[\\]]";
//    final String mes_name="不能为空";
    final String logo= "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
    final String mes_logo="地址必须已http或者ftp或https开头";
    final String firstLetter= "^[a-zA-Z]$";
    final String mes_firstLetter="检索首字母必须是一个字母";
}
