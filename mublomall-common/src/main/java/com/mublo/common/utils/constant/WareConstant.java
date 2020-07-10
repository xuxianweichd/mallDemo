/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.common.utils.constant;

import java.util.ArrayList;
import java.util.List;

public class WareConstant {

    public enum  PurchaseStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),FINISH(3,"已完成"),
        HASERROR(4,"有异常");
        private int code;
        private String msg;
        PurchaseStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }
        public static List<Integer> getReceive() {
            List<Integer> list = new ArrayList<>();
            list.add(PurchaseStatusEnum.CREATED.getCode());
            list.add(PurchaseStatusEnum.ASSIGNED.getCode());
            return list;
        }
        public static List<Integer> getNoReceive() {
            List<Integer> list = new ArrayList<>();
            list.add(PurchaseStatusEnum.RECEIVE.getCode());
            list.add(PurchaseStatusEnum.FINISH.getCode());
            list.add(PurchaseStatusEnum.HASERROR.getCode());
            return list;
        }

        public String getMsg() {
            return msg;
        }
    }


    public enum  PurchaseDetailStatusEnum{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),FINISH(3,"已完成"),
        HASERROR(4,"采购失败");
        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
        public static List<Integer> getReceive() {
            List<Integer> list = new ArrayList<>();
            list.add(PurchaseDetailStatusEnum.CREATED.getCode());
            list.add(PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return list;
        }
        public static List<Integer> getNoReceive() {
            List<Integer> list = new ArrayList<>();
            list.add(PurchaseDetailStatusEnum.BUYING.getCode());
            list.add(PurchaseDetailStatusEnum.FINISH.getCode());
            list.add(PurchaseDetailStatusEnum.HASERROR.getCode());
            return list;
        }
    }
}
