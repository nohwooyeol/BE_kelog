package com.kelog.kelog.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {


    private String username;

    private String account;

    private String password;

    private String passwordConfirm;

    private String usercomment;
}
