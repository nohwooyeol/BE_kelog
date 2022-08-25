package com.kelog.kelog.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.kelog.kelog.response.MemberResponseDto;
import com.kelog.kelog.response.PostAllByResponseDto;
import com.kelog.kelog.response.PostResponseDto;
import com.kelog.kelog.response.ResponseDto;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.domain.Tags;
import com.kelog.kelog.exception.CustomException;
import com.kelog.kelog.exception.ErrorCode;
import com.kelog.kelog.repository.HeartRepository;
import com.kelog.kelog.repository.TagsRepository;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.repository.PostRepository;
import com.kelog.kelog.request.PostRequestDto;
import com.kelog.kelog.security.UserDetailsServiceImpl;
import com.kelog.kelog.security.jwt.TokenProvider;
import com.kelog.kelog.shared.CommonUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    private final HeartRepository heartRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //게시물 작성
    @Transactional
    public ResponseDto<?> createPost(MultipartFile multipartFile, PostRequestDto requestDto, HttpServletRequest request) throws IOException {

//        Member member = memberRepository.getReferenceById(1L);

        if (request.getHeader("Authorization")==null){
            return ResponseDto.fail("NOT_FOUND", "로그인 해주세요.");

        }
        Member member = memberService.existMember(tokenProvider.getUserAccount(request));

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
                        .heartCount(post.getHeartCount())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    //게시글 상세보기
    @Transactional
    public ResponseDto<?> getPost(Long id, HttpServletRequest request){
        System.out.println("-----------------------------------------------------------------");
        System.out.println(request.getHeader("Authorization"));
        System.out.println("-----------------------------------------------------------------");
        Post post = isPresentPost(id);
        if (null == post){
            throw new CustomException(ErrorCode.POST_POST_NOT_FOUND_ERROR);
        }
        boolean existLike=false;
        if (!(request.getHeader("Authorization")==null)){
            String account = tokenProvider.getUserAccount(request);
            if (account==null) {
                existLike = false;
            } else {
                Long memberid = memberService.existMember(account).getId();
                existLike = heartRepository.existsAllByPostAndMemberId(post,memberid);
            }}


        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getPostId())
                        .title(post.getTitle())
                        .tags(post.getTags())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .heartCount(post.getHeartCount())
                        .heartPush(existLike)
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // 게시글 수정
    @Transactional
    public  ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request)
    {
        Member member = memberService.existMember(tokenProvider.getUserAccount(request));
        if (null == member) {
//            return ResponseDto.fail("NOT_FOUND", "로그인 해주세요.");
            throw new CustomException(ErrorCode.POST_ACCOUNT_NOT_FOUND_ERROR);
        }

        Post post = isPresentPost(id);
        if (null == post) {
            throw new CustomException(ErrorCode.POST_POST_NOT_FOUND_ERROR);
        }

        if (!(post.getMember() == member)) {
            throw new CustomException(ErrorCode.POST_POST_NOT_FOUND_ERROR);
        }

        List<Tags> tags = getTag(post);
        tagsRepository.deleteAll(tags);

        post.update(requestDto);

        return  ResponseDto.success(new PostResponseDto(post));
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long PostId, HttpServletRequest request)
    {


        Member member = memberService.existMember(tokenProvider.getUserAccount(request));
        if (null == member) {
//            return ResponseDto.fail("NOT_FOUND", "로그인 해주세요.");
            throw new CustomException(ErrorCode.POST_ACCOUNT_NOT_FOUND_ERROR);
        }

        Post post = isPresentPost(PostId);
        if (null == post) {
            throw new CustomException(ErrorCode.POST_POST_NOT_FOUND_ERROR);
        }

        if (!(post.getMember() == member)) {
            throw new CustomException(ErrorCode.POST_POST_NOT_FOUND_ERROR);
        }

        Long deleteid = post.getPostId();
        postRepository.delete(post);
        return ResponseDto.success(deleteid);
    }

    @Transactional
    public Post isPresentPost(Long id) {
        Optional<Post> optionalContent = postRepository.findById(id);
        return optionalContent.orElse(null);
    }

    // 전체 게시글 조회 (NEW)
    public List<PostAllByResponseDto> GetNewPost(int page, int size) {
        List<Post> postPaging = postRepository.findAllByPaging((PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
       return postPaging
                .stream()
                .map(PostAllByResponseDto::new)
                .collect(toList());
    }

    public List<PostAllByResponseDto> GetTodayPost(int page, int size) {
        LocalDateTime localDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size);
        List<Post> postPaging = postRepository.findAllByCreatedAtOrderByHeartCountDesc(localDate,pageable);
        return postPaging
                .stream()
                .map(PostAllByResponseDto::new)
                .collect(toList());
    }
    public List<PostAllByResponseDto> GetWeekPost(int page, int size) {
        LocalDateTime localDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size);
        List<Post> postPaging = postRepository.findAllByCreatedAtGreaterThanOrderByHeartCountDesc(localDate.minusDays(6),pageable);
        return postPaging
                .stream()
                .map(PostAllByResponseDto::new)
                .collect(toList());
    }
    public List<PostAllByResponseDto> GetMonthPost(int page, int size) {
        LocalDateTime localDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size);
        List<Post> postPaging = postRepository.findAllByCreatedAtGreaterThanOrderByHeartCountDesc(localDate.minusMonths(1),pageable);
        return postPaging
                .stream()
                .map(PostAllByResponseDto::new)
                .collect(toList());
    }
    public List<PostAllByResponseDto> GetYearPost(int page, int size) {
        LocalDateTime localDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size);
        List<Post> postPaging = postRepository.findAllByCreatedAtGreaterThanOrderByHeartCountDesc(localDate.minusYears(1),pageable);
        return postPaging
                .stream()
                .map(PostAllByResponseDto::new)
                .collect(toList());
    }

    //  내 게시물 전체 목록
    public List<PostAllByResponseDto> getmypost(HttpServletRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Member member = memberService.existMember(tokenProvider.getUserAccount(request));
        List<Post> findPostByMember = postRepository.findAllMemberId(member.getId(),pageable);
        List<PostAllByResponseDto> postResponseDto = findPostByMember
                .stream()
                .map(PostAllByResponseDto::new)
                .collect(toList());
        return postResponseDto;
    }

    private List<Tags> getTag(Post post) {
        return tagsRepository.findAllByPost(post);
    }
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.POST_MEMEBER_NOT_FOUND_ERROR));
    }
    private void validateMember(Member member, Long memberId) {
        if (!memberId.equals(member.getId())) {
            throw new CustomException(ErrorCode.POST_MEMBER_NOT_AUTH_ERROR);
        }
    }
    private void dupTag(List<Tags> tagsList) {
        for(Tags t : tagsList){
            Tags dupTag = tagsRepository.findTagsByTagNameAndPost(t.getTagName(),t.getPost());
            if(dupTag != null){
                //throw new IllegalArgumentException("중복된 태그");
                throw new CustomException(ErrorCode.POST_TAG_DUPLICATION_ERROR);
            }
        }
    }

//    게시글에서 유저 정보 불러오기
    public MemberResponseDto userinfo(Long id) {

        return new MemberResponseDto(postRepository.getReferenceById(id).getMember());
    }




}
