package com.kelog.kelog.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostAllByMemberResponseDto {
    private List<PostResponseDto> postResponseDto;
    private MemberResponseDto memberResponseDto;


    public PostAllByMemberResponseDto(List<PostResponseDto> postResponseDto, MemberResponseDto memberResponseDto) {
        this.postResponseDto = postResponseDto;
        this.memberResponseDto = memberResponseDto;
    }
}
