package com.kelog.kelog.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.domain.Tags;
import com.kelog.kelog.repository.TagsRepository;
import com.kelog.kelog.response.MemberResponseDto;
import com.kelog.kelog.response.PostAllByMemberResponseDto;
import com.kelog.kelog.response.PostResponseDto;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.request.PostRequestDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.security.UserDetailsServiceImpl;
import com.kelog.kelog.security.jwt.TokenProvider;
import com.kelog.kelog.shared.CommonUtils;
import com.kelog.kelog.tag.TagNameAndCount;
import com.kelog.kelog.tag.TagResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService{

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    private final TagsRepository tagsRepository;

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
//      Member member = memberService.existMember(tokenProvider.getUserAccount(request));

        String imgUrl = null;
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
            imgUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
            List<Tags> tags = Tags.createTag(requestDto.getTags());
            dupTag(tags);
        }


        Post post = Post.builder()
                .title(requestDto.getTitle())
                .tags(requestDto.getTags())
                .content(requestDto.getContent())
                .imgUrl(imgUrl)
                .heartCount(0L)
                .member(member)
                .build();
        postRepository.save(post);
        return  ResponseDto.success(
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

    //게시글 상세보기
    @Transactional
    public ResponseDto<?> getPost(Long id){
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
        List<Tags> tags = getTag(post);
        tagsRepository.deleteAll(tags);

        post.update(requestDto);
        return  ResponseDto.success(post);
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long PostId, PostRequestDto requestDto, HttpServletRequest request)
    {
        Post post = isPresentPost(PostId);
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

    // 전체 게시글 조회 (최적화)
    public List<PostResponseDto> getAllPost(int page, int size) {
        List<Post> postPaging = postRepository.findAllPaging(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return postPaging
                .stream()
                .map(PostResponseDto::new)
                .collect(toList());
    }

    //  내 게시물 전체 목록
    public PostAllByMemberResponseDto getMemberPost(Long memberId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Member member = getMemberById(memberId);
        System.out.println(member.getId());
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);

        List<Post> findPostingByMember = postRepository.findAllMemberId(memberId,pageable);
        System.out.println(member.getId());

        List<PostResponseDto> postResponseDto = findPostingByMember
                .stream()
                .map(PostResponseDto::new)
                .collect(toList());

        List<TagNameAndCount> tagList = tagsRepository.findAll(memberId);

        List<TagResponseDto> tagResponseDto = tagList
                .stream()
                .map(TagResponseDto::new)
                .collect(toList());

        return new PostAllByMemberResponseDto(postResponseDto,tagResponseDto,memberResponseDto);

    }

    private List<Tags> getTag(Post post) {
        return tagsRepository.findAllByPost(post);
    }
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("조회된 멤버가 없습니다"));
    }
    private void validateMember(Member member, Long memberId) {
        if (!memberId.equals(member.getId())) {
            throw new IllegalArgumentException("해당 게시물에 대한 수정 권한이 없습니다.");
        }
    }
    private void dupTag(List<Tags> tagsList) {
        for(Tags t : tagsList){
            Tags dupTag = tagsRepository.findTagsByTagNameAndPost(t.getTagName(),t.getPost());
            if(dupTag != null){
                throw new IllegalArgumentException("중복된 태그");
            }
        }
    }

}
