package com.kelog.kelog.controller;

import com.kelog.kelog.request.LoginDto;
import com.kelog.kelog.request.SignUpRequestDto;
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
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseBody
    public String Signup(@RequestPart(value = "file") MultipartFile multipartFile,
                         @RequestPart(value = "info") SignUpRequestDto requestDto) throws IOException {
        return memberService.Signup(multipartFile,requestDto);
    }

    @PostMapping("/login")
    @ResponseBody
    public String signin(@RequestBody LoginDto loginDto,HttpServletResponse response){

        return memberService.login(loginDto,response);
    }

    @GetMapping("/test")
    @ResponseBody
    public String Test(HttpServletRequest request){
        return memberService.test(request);
    }


}
