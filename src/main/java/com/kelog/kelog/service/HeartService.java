package com.kelog.kelog.service;

import com.kelog.kelog.domain.Heart;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.exception.CustomException;
import com.kelog.kelog.exception.ErrorCode;
import com.kelog.kelog.repository.HeartRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    private final PostRepository postRepository;

    private final HeartRepository heartRepository;

    //-----------------------------------------------------------------------------------------------------------------
    // 게시글 좋아요 & 취소
    public ResponseDto<?> Like(Long postId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
            throw new CustomException(ErrorCode.LIKE_JWT_NOT_FOUND_FAIL);
        }

        String account = tokenProvider.getUserAccount(request);
        Member member = memberService.existMember(account);
        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
            throw new CustomException(ErrorCode.LIKE_MEMBER_NOT_FOUND_FAIL);
        }
        // 게시글 존재 확인
        Post post = existPost(postId);
        if (null == post) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
            throw new CustomException(ErrorCode.LIKE_POST_NOT_FOUND_FAIL);
        }
        // 게시글 좋아요 생성 & 삭제
        Heart checkHeart = heartRepository.findByMemberIdAndPost(member.getId(),post);
        if(!(checkHeart == null)){
            // 게시글 좋아요 수 삭제
            heartRepository.deleteById(checkHeart.getId());
        } else {
            Heart heart = Heart.builder()
                    .memberId(member.getId())
                    .post(post)
                    .build();
            heartRepository.save(heart);
        }
        Long heart = heartRepository.countAllByPost(post);
        post.updateHeart(heart);
        postRepository.save(post);


        return ResponseDto.success(post.getHeartCount());

    }

    @Transactional(readOnly = true)
    public Post existPost(Long id) {
        Optional<Post> optionalPost = postRepository.findByPostId(id);
        return optionalPost.orElse(null);
    }
}
