package com.kelog.kelog.service;

import com.kelog.kelog.domain.Comment;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.exception.CustomException;
import com.kelog.kelog.exception.ErrorCode;
import com.kelog.kelog.repository.CommentRepository;
import com.kelog.kelog.request.CommentRequestDto;
import com.kelog.kelog.response.CommentCountResponseDto;
import com.kelog.kelog.response.CommentResponseDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.security.jwt.TokenProvider;
import com.kelog.kelog.util.CheckUtill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {


    private final CommentRepository commentRepository;

    private final CheckUtill checkUtill;

    private final TokenProvider tokenProvider;

    private final MemberService memberService;




    @Transactional
    public ResponseDto<?> createComment(Long postId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        String account = tokenProvider.getUserAccount(request);

        Member member = memberService.existMember(account);

        if(member==null){
           // return ResponseDto.fail("NOT_ACCOUNT","로그인을 해주세요");
            throw new CustomException(ErrorCode.COMMENT_ACCOUNT_NOT_FOUND_ERROR);
        }

        Post post = checkUtill.isPresentPost(postId);
        if(post==null){
            throw new CustomException(ErrorCode.COMMENT_POST_NOT_FOUND_ERROR);
        }

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .comment(commentRequestDto.getComment())
                .build();
        commentRepository.save(comment);
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .commentId(comment.getId())
                .username(comment.getMember().getUsername())
                .comment(comment.getComment())
                .profileimage(comment.getMember().getProfileimage())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
        return ResponseDto.success(responseDto,"댓글이 등록되었습니다.");
    }



    @Transactional
    public ResponseDto<?> getAllComment(Long postId) {


        Post post = checkUtill.isPresentPost(postId);
        if(post == null){
            //return ResponseDto.fail("POST_NOT_FOUND","게시글이 없습니다.");
            throw new CustomException(ErrorCode.COMMENT_POST_NOT_FOUND_ERROR);
        }


        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Long count = (long) commentList.size();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getId())
                            .username(comment.getMember().getUsername())
                            .comment(comment.getComment())
                            .memberId(post.getMember().getId())
                            .profileimage(comment.getMember().getProfileimage())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        CommentCountResponseDto commentCountResponseDto =CommentCountResponseDto.builder()
                .commentcount(count)
                .responseDto(commentResponseDtoList)
                .build();

        return ResponseDto.success(commentCountResponseDto,"댓글 조회완료");
    }



    @Transactional
    public ResponseDto<?> updateComment(Long commentId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {

        String account = tokenProvider.getUserAccount(request);

        Member member = memberService.existMember(account);

        if(member==null) {
            //return ResponseDto.fail("NOT_ACCOUNT", "로그인을 해주세요");
            throw new CustomException(ErrorCode.COMMENT_ACCOUNT_NOT_FOUND_ERROR);
        }

        Comment comment = checkUtill.isPresentComment(commentId);
        if(comment == null){
            //return ResponseDto.fail("NOT_COMMENT","댓글이 없습니다");
            throw new CustomException(ErrorCode.COMMENT_COMMENT_NOT_FOUND_ERROR);
        }

        comment.update(commentRequestDto);
        System.out.println(comment.getMember().getProfileimage());
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getId())
                        .username(comment.getMember().getUsername())
                        .comment(comment.getComment())
                        .profileimage(comment.getMember().getProfileimage())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                .build(),
                "댓글 수정완료");
    }




    @Transactional
    public ResponseDto<?> deleteComment(Long commentId,
                                        HttpServletRequest request) {
        String account = tokenProvider.getUserAccount(request);
        Member member = memberService.existMember(account);
        if(member==null){
            //return ResponseDto.fail("NOT_ACCOUNT","로그인을 해주세요");
            throw new CustomException(ErrorCode.COMMENT_ACCOUNT_NOT_FOUND_ERROR);
        }
        Comment comment = checkUtill.isPresentComment(commentId);
        if(comment == null){
            //return ResponseDto.fail("NOT_COMMENT","댓글이 없습니다");
            throw new CustomException(ErrorCode.COMMENT_COMMENT_NOT_FOUND_ERROR);
        }

        commentRepository.delete(comment);

        return ResponseDto.success(commentId,"댓글 삭제되었습니다");
    }
}
