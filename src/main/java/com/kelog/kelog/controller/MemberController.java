package com.kelog.kelog.controller;

import com.kelog.kelog.request.IdCheckDto;
import com.kelog.kelog.request.LoginDto;
import com.kelog.kelog.request.SignUpRequestDto;
import com.kelog.kelog.response.MemberResponseDto;
import com.kelog.kelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api", produces = "application/json; charset=utf8")
public class MemberController {

    private final MemberService memberService;
    //  회원가입
    @PostMapping("/register")
    @ResponseBody
    public String Signup(@RequestPart(value = "file") MultipartFile multipartFile,
                         @RequestPart(value = "info") SignUpRequestDto requestDto) throws IOException {
        return memberService.Signup(multipartFile,requestDto);
    }
    //  로그인
    @PostMapping("/login")
    @ResponseBody
    public String signin(@RequestBody LoginDto loginDto,HttpServletResponse response){

        return memberService.login(loginDto,response);
    }
    //  현재 로그인 유저 테스트용
    @GetMapping("/test")
    @ResponseBody
    public String Test(HttpServletRequest request){
        return memberService.test(request);
    }


    @PostMapping("/idcheck")
    @ResponseBody
    public boolean idcheck(@RequestBody IdCheckDto idCheckDto) {
       return memberService.accountCheck(idCheckDto.getAccount());
    }


}
