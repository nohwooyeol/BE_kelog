package com.kelog.kelog.service;

import com.kelog.kelog.domain.Comment;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.repository.CommentRepository;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.request.CommentRequestDto;
import com.kelog.kelog.response.CommentResponseDto;
import com.kelog.kelog.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Component
public class CommentService {
    
    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;




    @Transactional
    public ResponseDto<?> createComment(Long postId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {

        //유저 체크후
        //포스트 체크후 객체에 담기



        Comment comment = Comment.builder()
 //               .post(postId)
 //               .username(member.getaccount())
                .comment(commentRequestDto.getComment())
                .build();

        CommentResponseDto responseDto = CommentResponseDto.builder()
                .commentId(comment.getId())
                //.username(comment.getusername())
                .comment(comment.getComment())
                .build();
        return ResponseDto.success(responseDto,"댓글이 등록되었습니다.");
    }



    @Transactional
    public ResponseDto<?> getComment(Long postId,
                                     HttpServletRequest request) {
        
        // 아이디 조회



        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            return ResponseDto.fail("POST_NOT_FOUND","게시글이 없습니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        int count = commentList.size();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getId())
                            .username(comment.getMember().getUsername())
                            .comment(comment.getComment())
                            .memberId(comment.getMember().getId())
                            .build()
            );
        }

        return ResponseDto.success(commentResponseDtoList,"댓글 조회완료");
    }



    @Transactional
    public ResponseDto<?> updateComment(Long commentId,
                                        CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        //회원 정보확인


        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.orElse(null);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
        }
        comment.update(commentRequestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                .commentId(comment.getId())
                .build(),
                "댓글 수정완료");
    }




    @Transactional
    public ResponseDto<?> deleteComment(Long commentId,
                                        HttpServletRequest request) {

        //회원 정보 확인


        commentRepository.deleteById(commentId);

        return ResponseDto.success(true,"댓글 삭제완료");
    }
}
