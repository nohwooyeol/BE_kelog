package com.kelog.kelog.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private Long memberId;
    private String title;
    private List<String> Tags;
    private String content;
    private String imgUrl;
    private Long heart;

    @Getter
    public static class createTags{
        public List<String> stringTagName = new ArrayList<>();
    }
}
