/*
 * Copyright (c) 2020.
 * 作者：mublo
 * 邮箱：XuXianYu.Transo@outlook.com
 * 日期：2020-07-10 22:55
 */

package com.mublo.mublomall.auth.to;
import com.mublo.common.utils.constant.messageConstant;
import com.mublo.common.utils.constant.regExpConstant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author: mublo
 * @Date: 2020/7/10 22:55
 * @Version 1.0
 * @website https://github.com/xuxianweichd/mallDemo
 */
@Data
public class RegisterTo {
    @NotBlank(message = messageConstant.nullUserMsg)
    String username;
    @NotBlank(message = messageConstant.nullPhoneMsg)
    @Pattern(regexp = regExpConstant.phoneRegExp, message = messageConstant.errorRegExpPhoneMsg)
    String phone;
    @NotBlank(message = messageConstant.nullPasswordMsg)
    @Pattern(regexp = regExpConstant.passwordRegExp,message = messageConstant.errorRegExpPasswordMsg)
    String password;
    @NotBlank(message = messageConstant.nullPasswordMsg)
    @Pattern(regexp = regExpConstant.passwordRegExp,message = messageConstant.errorRegExpPasswordMsg)
    String againPassword;
    @NotBlank(message = messageConstant.nullCodeMsg)
    String smsCode;
}
