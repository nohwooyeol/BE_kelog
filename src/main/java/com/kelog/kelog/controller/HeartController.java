package com.kelog.kelog.controller;

import com.kelog.kelog.repository.HeartRepository;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;



    @PostMapping("/postheart/{id}")
    @ResponseBody
    public ResponseDto<?> Like(@PathVariable Long id, HttpServletRequest request){

        return heartService.Like(id,request);

    }




}
