package com.wang.blog.vo.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 王家俊
 */
@Data
public class SsoParams {
    @NotNull(message = "请输入用户名")
    @NotEmpty(message = "用户名不可以为空")
    private String account;


    @NotNull(message = "请输入密码")
    @NotEmpty(message = "用户名不可以为空")
    @Length(min = 6,max = 12,message = "传入的密码长度不正确")
    private String password;

    @NotNull(message = "请输入昵称")
    @NotEmpty(message = "昵称不可以为空")
    private String nickname;
}
