/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
//Constraint里面的validatedBy指定校验器
@Constraint(
        validatedBy = {ListValueConstraintValidator.class}
)
//Target配置注解可使用范围
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//Repeatable同一个注解能在使用范围内的同一个对象上多次使用
//@Repeatable(NotBlank.List.class)
public @interface ListValue {
    String message() default "{com.atguigu.common.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    int[] value() default {};
}
