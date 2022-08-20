package com.kelog.kelog.service;


import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.response.PostResponseDto;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.request.PostRequestDto;
import com.kelog.kelog.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService{

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;




    //게시물 작성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request)
    {


        Member member = memberRepository.getReferenceById(1L);

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .tags(requestDto.getTags())
                .content(requestDto.getContent())
                .imgUrl(requestDto.getImgUrl())
                .member(member)
                .build();
        postRepository.save(post);
        return  ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getPostId())
                        .tags(post.getTags())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    //게시글 상세보기
    @Transactional
    public ResponseDto<?>getPost(Long id){
        Post post = isPresentPost(id);
        if (null == post){
            return ResponseDto.fail("NOT_POST", "게시글을 찾을 수 없습니다.");
        }
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getPostId())
                        .title(post.getTitle())
                        .tags(post.getTags())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()

        );
    }

    // 게시글 수정
    @Transactional
    public  ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request)
    {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        post.update(requestDto);
        return  ResponseDto.success(post);
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, PostRequestDto requestDto, HttpServletRequest request)
    {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        postRepository.delete(post);
        return ResponseDto.success("Delete success");
    }

    @Transactional
    public Post isPresentPost(Long id) {
        Optional<Post> optionalContent = postRepository.findById(id);
        return optionalContent.orElse(null);
    }

}
