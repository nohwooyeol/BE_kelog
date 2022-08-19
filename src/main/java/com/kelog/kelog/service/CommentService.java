package com.kelog.kelog.service;

import com.kelog.kelog.domain.Comment;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.repository.CommentRepository;
import com.kelog.kelog.request.CommentRequestDto;
import com.kelog.kelog.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class CommentService {
    
    private final CommentRepository commentRepository;


    
    public ResponseDto<?> createComment(Long postId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {

        //유저 체크후

        Comment comment = Comment.builder()
               // .post(postId)
                .comment(commentRequestDto.getComment())
                .build();




        return ResponseDto.success("댓글생성완료");
    }

    public ResponseDto<?> getComment(Long postId,
                                     HttpServletRequest request) {

        return ResponseDto.success("댓글조회완료");
    }


    public ResponseDto<?> updateComment(Long commentId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {

        return ResponseDto.success("댓글 수정완료");
    }


    public ResponseDto<?> deleteComment(Long commentId,
                                        HttpServletRequest request) {

        return ResponseDto.success("댓글 삭제완료");
    }
}
