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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final AmazonS3Client amazonS3Client;

    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public String Signup(MultipartFile multipartFile, SignUpRequestDto requestDto) throws IOException {
        System.out.println("-----------------------------");

        String imgurl;

        if (!multipartFile.isEmpty()){
            String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();
        } else {
            imgurl = "https://fortestblog.s3.amazonaws.com/profileimage.png";
        }
        System.out.println("12313123");
        Member newMember = new Member(requestDto,imgurl);

        memberRepository.save(newMember);

        return "회원가입에 성공했습니다!";
    }
}
