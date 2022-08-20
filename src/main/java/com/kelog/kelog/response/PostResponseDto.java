package com.kelog.kelog.response;

import com.kelog.kelog.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private List<String> tags;
    private String content;
    private String imgUrl;
    private Long heartCount;
    private LocalDate createdAt;
    private LocalDate modifiedAt;
}
