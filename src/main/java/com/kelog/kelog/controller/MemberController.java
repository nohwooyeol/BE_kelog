package com.kelog.kelog.controller;

import com.kelog.kelog.request.SignUpRequestDto;
import com.kelog.kelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseBody
    public String Signup(@RequestPart(required = false,value = "file") MultipartFile multipartFile,
                         @RequestPart(value = "info")SignUpRequestDto requestDto) throws IOException {
        System.out.println("---------------------------------------------------------");

        return memberService.Signup(multipartFile,requestDto);
    }


}
