package com.kelog.kelog.tag;

import com.kelog.kelog.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagNameAndCount {
    private Long id;
    private Long tagId;
    private String tagName;
    private Post post;
    private Long count;
}
