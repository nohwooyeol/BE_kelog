package com.kelog.kelog.service;

import com.kelog.kelog.domain.Comment;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.repository.CommentRepository;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.request.CommentRequestDto;
import com.kelog.kelog.response.CommentResponseDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.Security.jwt.TokenProvider;
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

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;

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
            return ResponseDto.fail("NOT_ACCOUNT","로그인을 해주세요");
        }

        Post post = checkUtill.isPresentPost(postId);
        if(post==null){
            return ResponseDto.fail("NOT_POST","게시글이 없습니다.");
        }


        Comment comment = Comment.builder()
                .post(post)
                .username(member.getUsername())
                .comment(commentRequestDto.getComment())
                .build();

        commentRepository.save(comment);

        CommentResponseDto responseDto = CommentResponseDto.builder()
                .commentId(comment.getId())
                .username(comment.getUsername())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        return ResponseDto.success(responseDto,"댓글이 등록되었습니다.");
    }



    @Transactional
    public ResponseDto<?> getAllComment(Long postId,
                                     HttpServletRequest request) {

        String account = tokenProvider.getUserAccount(request);

        Member member = memberService.existMember(account);

        if(member==null){
            return ResponseDto.fail("NOT_ACCOUNT","로그인을 해주세요");
        }

        Post post = checkUtill.isPresentPost(postId);
        if(post == null){
            return ResponseDto.fail("POST_NOT_FOUND","게시글이 없습니다.");
        }


        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Long count = (long) commentList.size();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getId())
                            .username(comment.getUsername())
                            .comment(comment.getComment())
                            .memberId(post.getMember().getId())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(commentResponseDtoList,"댓글 조회완료");
    }



    @Transactional
    public ResponseDto<?> updateComment(Long commentId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {

        String account = tokenProvider.getUserAccount(request);

        Member member = memberService.existMember(account);

        if(member==null) {
            return ResponseDto.fail("NOT_ACCOUNT", "로그인을 해주세요");
        }

        Comment comment = checkUtill.isPresentComment(commentId);
        if(comment == null){
            return ResponseDto.fail("NOT_COMMENT","댓글이 없습니다");
        }

        comment.update(commentRequestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .commentId(comment.getId())
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
            return ResponseDto.fail("NOT_ACCOUNT","로그인을 해주세요");
        }


        Comment comment = checkUtill.isPresentComment(commentId);
        if(comment == null){
            return ResponseDto.fail("NOT_COMMENT","댓글이 없습니다");
        }

        commentRepository.delete(comment);

        return ResponseDto.success(true,"댓글 삭제되었습니다");
    }
}
