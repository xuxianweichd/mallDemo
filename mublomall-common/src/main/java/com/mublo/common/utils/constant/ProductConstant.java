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
}
