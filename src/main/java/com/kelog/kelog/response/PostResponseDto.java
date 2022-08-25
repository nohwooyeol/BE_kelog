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
    private boolean heartPush;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.tags = post.getTags();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.heartCount = post.getHeartCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

}
