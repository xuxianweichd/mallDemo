/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-09 18:33
 */

package com.mublo.common.utils.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: mublo
 * @Date: 2020/7/9 18:33
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String phone;
    String call;
    String operationType;
    String code;
    int time;
    int type;
}
