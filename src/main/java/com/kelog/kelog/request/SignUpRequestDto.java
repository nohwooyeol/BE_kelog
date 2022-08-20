package com.kelog.kelog.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequestDto {
    String username;
    String account;
    String password;
    String passwordConfirm;
    String usercomment;

}
