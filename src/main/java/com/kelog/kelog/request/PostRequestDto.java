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
    private String title;
    private List<String> tags = new ArrayList<>();
    private String content;

//    @Getter
//    public static class createTags{
//        public List<String> stringTagName = new ArrayList<>();
//    }
}
