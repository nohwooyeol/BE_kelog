package com.kelog.kelog.controller;


import com.kelog.kelog.request.PostRequestDto;
import com.kelog.kelog.response.PostAllByMemberResponseDto;
import com.kelog.kelog.response.PostAllByResponseDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    //게시글전체 (메인페이지)
    @GetMapping("/post")
    public List<PostAllByResponseDto> getAllPost(
                                            @RequestParam(value = "page",defaultValue = "1") int page,
                                            @RequestParam(value = "size",defaultValue = "20") int size){

        return postService.GetNewPost(page,size);
    }
    //게시글 해당 멤버 작성글 보기
    @GetMapping("/{memberId}")
    public List<PostAllByResponseDto> getMemberPostings(
            @PathVariable Long memberId,
            @RequestParam("page") int page,
            @RequestParam("size") int size){


        return postService.getMemberPost(memberId, page, size);
    }

    //게시글 상세보기
    @GetMapping("/post/{id}")
    private ResponseDto<?> getPost(@PathVariable Long id, HttpServletRequest request) {
        return postService.getPost(id, request);
    }


    //게시글 작성
    @PostMapping("/post")
    public ResponseDto<?> createPost(@RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                     @RequestPart(value = "info") PostRequestDto postRequestDto,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(multipartFile, postRequestDto, request);
    }

    //게시글 수정
    @PutMapping("/post/update/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("/post/delete/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.deletePost(id, postRequestDto, request);
    }

}
