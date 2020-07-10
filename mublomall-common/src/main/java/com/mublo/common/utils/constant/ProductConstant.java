/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.common.utils.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ProductConstant {
    public enum  AttrEnum{
        ATTR_TYPE_SALE_OR_BASE(2,"即是销售属性又是基本属性"),
        ATTR_TYPE_BASE(1,"基本属性"),
        ATTR_TYPE_SALE(0,"销售属性"),
        ;
        private int code;
        private String msg;
        AttrEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        public int getCode() {
            return code;
        }
        public static List<Integer> getCode(String type) {
            List<Integer> code=new ArrayList<>();
            if (type.equalsIgnoreCase("base")){
                code.add(AttrEnum.ATTR_TYPE_BASE.getCode());
                code.add(AttrEnum.ATTR_TYPE_SALE_OR_BASE.getCode());
            }else if (type.equalsIgnoreCase("sale")){
                code.add(AttrEnum.ATTR_TYPE_SALE.getCode());
                code.add(AttrEnum.ATTR_TYPE_SALE_OR_BASE.getCode());
            }else {
                code.add(AttrEnum.ATTR_TYPE_SALE_OR_BASE.getCode());
            }
            return code;
        }

        public String getMsg() {
            return msg;
        }

        //获取枚举实例
//        public static AttrEnum fromcode(String type) {
////            .values()获取枚举类所有对象
//            for (AttrEnum statusEnum : AttrEnum.values()) {
//                if (Objects.equals(type, statusEnum.getMsg())) {
//                    return statusEnum;
//                }
//            }
//            throw new IllegalArgumentException();
//        }
    }
    public enum StatusEnum{
        NEA_SPU(0,"新建"),SPU_UP(1,"商品上架"),SPU_DOWN(2,"商品下架");
        private int code;
        private String message;

        StatusEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
