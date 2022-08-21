package com.kelog.kelog.controller;


import com.kelog.kelog.request.PostRequestDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    //게시글 상세보기
    @GetMapping("/{id}")
    private ResponseDto<?> getPost(@PathVariable Long id){
        return postService.getPost(id);
    }


    //게시글 작성
    @PostMapping("/")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto,
                                  HttpServletRequest request){
        return postService.createPost(postRequestDto, request);
    }

    //게시글 수정
    @PutMapping("/update/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request){
        return postService.updatePost(id, postRequestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request){
        return postService.deletePost(id, postRequestDto, request);
    }

}
