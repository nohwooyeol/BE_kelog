package com.kelog.kelog.controller;


import com.kelog.kelog.request.PostRequestDto;
import com.kelog.kelog.response.MemberResponseDto;
import com.kelog.kelog.response.PostAllByResponseDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.service.PostService;
import com.kelog.kelog.shared.QueryEnum;
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
    public List<PostAllByResponseDto> getAllPost(@RequestParam(value = "Category",defaultValue = "NEW")QueryEnum queryEnum,
                                                 @RequestParam(value = "page",defaultValue = "0") int page,
                                                 @RequestParam(value = "size",defaultValue = "20") int size,
                                                 HttpServletRequest request){
        switch (queryEnum) {
            case NEW:
                return postService.GetNewPost(page, size);
            case TODAY:
                return postService.GetTodayPost(page,size);
            case WEEK:
                return postService.GetWeekPost(page,size);
            case MONTH:
                return postService.GetMonthPost(page,size);
            case YEAR:
                return postService.GetYearPost(page,size);
            case MYPOST:
                return postService.getmypost(request, page,size);
        }
        return postService.GetNewPost(page, size);
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
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return postService.deletePost(id,request);
    }

    @GetMapping("/info/{postId}")
    @ResponseBody
    public MemberResponseDto userinfo(@PathVariable Long postId){
        return postService.userinfo(postId);
    }

}
