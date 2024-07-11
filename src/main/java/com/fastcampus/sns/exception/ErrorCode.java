package com.fastcampus.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "user name is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");

    private HttpStatus status;
    private String message;
}
