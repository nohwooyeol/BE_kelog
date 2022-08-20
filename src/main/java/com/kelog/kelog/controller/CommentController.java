package com.kelog.kelog.controller;

import com.kelog.kelog.request.CommentRequestDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request ){
        return commentService.createComment(postId,commentRequestDto,request);
    }

    //댓글 조회
    @GetMapping("/{postId}")
    public ResponseDto<?> getAllComment(@PathVariable Long postId,
                                 HttpServletRequest request ){
        return commentService.getAllComment(postId,request);
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request ){
        return commentService.updateComment(commentId,commentRequestDto,request);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId,
                                        HttpServletRequest request ){
        return commentService.deleteComment(commentId,request);
    }




}
