package com.kelog.kelog.response;

import com.kelog.kelog.tag.TagResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostAllByMemberResponseDto {
    private List<PostResponseDto> postResponseDto;
    private List<TagResponseDto> tagList;
    private MemberResponseDto memberResponseDto;


    public PostAllByMemberResponseDto(List<PostResponseDto> postResponseDto, List<TagResponseDto> tagResponseDto, MemberResponseDto memberResponseDto) {
        this.postResponseDto = postResponseDto;
        this.tagList = tagResponseDto;
        this.memberResponseDto = memberResponseDto;
    }
}
