package com.kelog.kelog.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.exception.CustomException;
import com.kelog.kelog.exception.ErrorCode;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.request.LoginDto;
import com.kelog.kelog.request.SignUpRequestDto;
import com.kelog.kelog.response.MemberResponseDto;
import com.kelog.kelog.security.jwt.TokenProvider;
import com.kelog.kelog.shared.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AmazonS3Client amazonS3Client;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;


    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String Signup(MultipartFile multipartFile, SignUpRequestDto requestDto) throws IOException {
//        ----------------------------------------------------------------------------------------------------
//        유효성검사 부분
//        아이디 패턴
//        아이디 확인
        if (requestDto.getAccount() == null) {
            //return "아이디를 적어주세요";
            throw new CustomException(ErrorCode.SIGNUP_ACCOUNT_NOT_FOUND_ERROR);
        }
//        닉네임 확인
        if (requestDto.getUsername() == null) {
            //return "아이디를 적어주세요";
            throw new CustomException(ErrorCode.SIGNUP_USERNAME_NOT_FOUND_ERROR);
        }
//        동일 account 확인
        if (memberRepository.existsByAccount(requestDto.getAccount())) {
            //return "이미 존재하는 아이디입니다.";
            throw new CustomException(ErrorCode.SIGNUP_ACCOUNT_DUPLICATE_ERROR);
        }
//        비밀번호 confirm 확인
        if (!Objects.equals(requestDto.getPassword(), requestDto.getPasswordConfirm())) {
            //return "비밀번호가 일치하지 않습니다.";
            throw new CustomException(ErrorCode.SIGNUP_PASSWORD_CHECK_ERROR);
        }
//        한줄평 확인
        if (requestDto.getUsercomment() == null) {
           //return "한줄평을 적어주세요!";
            throw new CustomException(ErrorCode.SIGNUP_USERCOMMENT_NOT_FOUND_ERROR);
        }
//        이미지 유무 확인
        if (multipartFile.isEmpty()){
            throw new CustomException(ErrorCode.SIGNUP_USERIMAGE_NOT_FOUND_ERROR);
        }
//        ---------------------------------------------------------------------------------------------------

//        이미지 업로드
        String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();
//        이미지 url 가져오기

//        비밀번호 암호화
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));

//        멤버 엔티티 생성
        Member newMember = new Member(requestDto, imgurl);

//        멤버 엔티티 저장
        memberRepository.save(newMember);

        return "회원가입에 성공했습니다!";
    }

    public MemberResponseDto login(LoginDto loginDto, HttpServletResponse response) {
        Member member = existMember(loginDto.getAccount());
        System.out.println("--------------------------------------------------------");
        System.out.println(member.getAccount());
        System.out.println(loginDto.getAccount());
        System.out.println("--------------------------------------------------------");

        if (null == member) {
            //return "존재하지 않는 사용자입니다.";
            throw new CustomException(ErrorCode.LOGIN_ACCOUNT_NOT_FOUND_FAIL);
        }
        if (!passwordEncoder.matches(loginDto.getPassword(),member.getPassword())) {
           // return "비밀번호가 맞지않습니다.";
            throw new CustomException(ErrorCode.LOGIN_PASSWORD_NOT_FOUND_FAIL);
        }
        String Token = tokenProvider.createToken(member);
        response.addHeader("Authorization",Token);
        return new MemberResponseDto(member);
    }
//    멤버 조회용
    public Member existMember(String account){
        Optional<Member> member = memberRepository.findByAccount(account);
        return member.orElse(null);
    }

//    토큰 실험용
    public String test(HttpServletRequest request) {
        return tokenProvider.getUserAccount(request);
    }
// 아이디 체크
    public boolean accountCheck(String account) {
        return memberRepository.existsAllByAccount(account);
    }

}
