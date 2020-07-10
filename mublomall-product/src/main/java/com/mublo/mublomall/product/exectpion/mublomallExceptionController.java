/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-07 17:23
 */

package com.mublo.mublomall.product.exectpion;

import com.mublo.common.utils.R;
import com.mublo.common.utils.exectpion.BizCodeEnume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.mublo.mublomall.product.controller")
public class mublomallExceptionController {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e) {
        log.error("数据效验出现问题｛" + e.getMessage() + "｝,异常类型：｛" + e.getClass() + "｝");
        BindingResult bindingResult = e.getBindingResult();
        Map<String, Object> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleThrowable(Throwable throwable) {
//        log.error("错误｛" + throwable.getStackTrace() + "｝,异常类型：｛" + throwable.getSuppressed() + "｝");
//        log.error("错误｛" + throwable.getCause() + "｝,异常类型：｛" + throwable.getLocalizedMessage() + "｝");
        log.error("错误｛" + throwable.getMessage() + "｝,异常类型：｛" + throwable.getClass() + "｝");
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
//    @ExceptionHandler(value = NullPointerException.class)
//    public R handleThrowable(NullPointerException n) {
//        n.get
//        log.error("错误｛" + throwable.getMessage() + "｝,异常类型：｛" + throwable.getClass() + "｝");
//        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
//    }
    @ExceptionHandler(value = RuntimeException.class)
    public R handleThrowable(RuntimeException r) {
        log.error("错误｛" + r.getMessage() + "｝,异常类型：｛" + r.getClass() + "｝");
        return R.error(500, r.getMessage());
    }
}
