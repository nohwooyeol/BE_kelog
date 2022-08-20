package com.kelog.kelog.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.repository.MemberRepository;
import com.kelog.kelog.request.SignUpRequestDto;
import com.kelog.kelog.shared.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AmazonS3Client amazonS3Client;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @ExceptionHandler
    public String handle(IOException ex) {
        return "오류발생";
    }


    public String Signup(MultipartFile multipartFile, SignUpRequestDto requestDto) throws IOException {
//        ----------------------------------------------------------------------------------------------------
//        유효성검사 부분
//        아이디 패턴
        String pattern = "[a-zA-Z\\d!@#$%^&*]*$";
        if (!Pattern.matches(pattern, requestDto.getAccount())|| requestDto.getAccount().length()<8 || requestDto.getAccount().length() > 30) {
            return "아이디 양식이 틀립니다.";
        }
//        비밀번호 패턴
        if (!Pattern.matches(pattern, requestDto.getPassword())|| requestDto.getPassword().length()<8 || requestDto.getPasswordConfirm().length() > 30) {
            return "비밀번호 양식이 틀립니다.";
        }
//        아이디 확인
        if (requestDto.getAccount() == null) {
            return "아이디를 적어주세요";
        }
//        닉네임 확인
        if (requestDto.getUsername() == null) {
            return "아이디를 적어주세요";
        }
//        동일 account 확인
        if (memberRepository.existsByAccount(requestDto.getAccount())) {
            return "이미 존재하는 아이디입니다.";
        }
//        비밀번호 confirm 확인
        if (!Objects.equals(requestDto.getPassword(), requestDto.getPasswordConfirm())) {
            return "비밀번호가 일치하지 않습니다.";
        }
//        한줄평 확인
        if (requestDto.getUsercomment() == null) {
            return "한줄평을 적어주세요!";
        }
//        이미지 유무 확인
        if (multipartFile.isEmpty()){
            return "이미지가 없습니다!";
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
}
