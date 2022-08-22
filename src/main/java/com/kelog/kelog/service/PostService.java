package com.kelog.kelog.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.response.PostResponseDto;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.security.request.PostRequestDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.security.UserDetailsServiceImpl;
import com.kelog.kelog.security.jwt.TokenProvider;
import com.kelog.kelog.shared.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService{

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    private final MemberService memberService;

    private final UserDetailsServiceImpl userDetailsService;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //게시물 작성
    @Transactional
    public ResponseDto<?> createPost(MultipartFile multipartFile, PostRequestDto requestDto, HttpServletRequest request) throws IOException {

        Member member = memberRepository.getReferenceById(1L);
//        Member member = memberService.existMember(tokenProvider.getUserAccount(request));

        String imgurl = null;
//        !multipartFile.isEmpty 쓸 때 오류!  빈값이 아닌 null 오면 오류 생김!
        if (!(multipartFile == null)){
            String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();
        }


        Post post = Post.builder()
                .title(requestDto.getTitle())
                .tags(requestDto.getTags())
                .content(requestDto.getContent())
                .imgUrl(imgurl)
                .heartCount(0L)
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

    @Transactional
    public ResponseDto<?>getPostList(){
        List<Post> postList = postRepository.findAll();
        List<PostResponseDto> List = new ArrayList<>();
        for (Post post: postList) {
            List.add(PostResponseDto.builder()
                    .id(post.getPostId())
                    .title(post.getTitle())
                    .tags(post.getTags())
                    .content(post.getContent())
                    .imgUrl(post.getImgUrl())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build());
        }
        return ResponseDto.success(List);
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
