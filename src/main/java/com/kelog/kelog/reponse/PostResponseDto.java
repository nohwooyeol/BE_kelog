package com.kelog.kelog.reponse;

import com.kelog.kelog.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
