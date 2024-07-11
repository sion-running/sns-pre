package com.fastcampus.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 회원가입 시
@Getter
@AllArgsConstructor
public class UserLoginRequest {
    private String userName;
    private String password;
}
