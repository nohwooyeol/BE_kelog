package com.kelog.kelog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kelog.kelog.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;


    @Column(nullable = false)
    private String title;

    @ElementCollection
    @Column
    private List<String> tags;

    @Column(nullable = false)
    private String content;

    @Column
    private String imgUrl;

    @Column
    private Long heartCount;

    @JsonIgnore
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.tags = postRequestDto.getTags();
        this.content = postRequestDto.getContent();
    }


}