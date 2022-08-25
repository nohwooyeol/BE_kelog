package com.kelog.kelog.response;

import com.kelog.kelog.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostAllByResponseDto {
    private Long id;
    private String title;
    private List<String> tags;
    private String content;
    private String imgUrl;
    private Long heartCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String account;
    private String profileimage;

    public PostAllByResponseDto(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.tags = post.getTags();
        this.content = post.getContent();
        this.imgUrl = post.getImgUrl();
        this.heartCount = post.getHeartCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.account = post.getMember().getAccount();
        this.profileimage = post.getMember().getProfileimage();
    }

}
