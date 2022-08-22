package com.kelog.kelog.domain;

import com.kelog.kelog.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment extends Timestamped{

    @Id //comment_id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment; //내용

    @Column(nullable = false)
    private String username; //닉네임


    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) //게시글하나에 댓글이 여러개 연관관계
    private Post post;

//    @JoinColumn(name ="member_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member member;  // 댓글을 작성한 회원

    public void update(CommentRequestDto commentRequestDto){this.comment = commentRequestDto.getComment();}



}
