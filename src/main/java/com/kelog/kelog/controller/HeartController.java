package com.kelog.kelog.controller;

import com.kelog.kelog.repository.HeartRepository;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;



    @PostMapping("/postheart/{id}")
    public ResponseDto<?> Like(Long id, HttpServletRequest request){


        return heartService.Like(id,request);


    }




}
