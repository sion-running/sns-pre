package com.fastcampus.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입 시
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
}
