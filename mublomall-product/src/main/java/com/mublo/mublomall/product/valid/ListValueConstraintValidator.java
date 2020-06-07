package com.mublo.mublomall.product.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private Set<Integer> set=new HashSet<>();
    @Override
    public void initialize(ListValue constraintAnnotation) {
        //获取到注解的值
        int[] value=constraintAnnotation.value();
        for(int val: value){

            set.add(val);
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
//        if (integer==null){
//
//        }
        System.out.println("integer"+integer);
//        查看set内是否包含
        return set.contains(integer);
    }
}
