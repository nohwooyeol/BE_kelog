package com.kelog.kelog.tag;

import com.kelog.kelog.domain.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDto {
    private Long tagId;
    private Long postId;
    private String tagName;
    private Long count;

    public TagResponseDto(Tags tags) {
        this.tagId =tags.getTagId();
        this.postId = tags.getPost().getPostId();
        this.tagName =tags.getTagName();
    }

    public TagResponseDto(TagNameAndCount tag) {
        this.tagId = tag.getTagId();
        this.tagName =tag.getTagName();
        this.postId =tag.getPost().getPostId();
        this.count =tag.getCount();
    }
}
