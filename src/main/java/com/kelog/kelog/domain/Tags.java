package com.kelog.kelog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tags {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long tagId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @Column
    private String tagName;

    public Tags(String tagName) {
        this.tagName=tagName;
    }

    public void addPost(Post post){
        this.post = post;
    }

    //== 태그 생성 ==//
    public static List<Tags> createTag(List<String> stringTagName) {
        return stringTagName.stream()
                .map(Tags::new)
                .collect(Collectors.toList());
    }

    //== 태그 수정 ==//
    public static List<Tags> updateTag(List<String> stringTagName) {

        return stringTagName.stream()
                .map(Tags::new)
                .collect(Collectors.toList());
    }
}
