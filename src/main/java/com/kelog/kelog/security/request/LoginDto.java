package com.kelog.kelog.security.request;

import lombok.Getter;

@Getter
public class LoginDto {
    String account;
    String password;
}
