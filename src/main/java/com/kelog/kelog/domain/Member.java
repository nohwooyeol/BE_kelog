package com.kelog.kelog.domain;

import com.kelog.kelog.request.SignUpRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String account;

    @Column
    private String password;

    @Column
    private String username;

    @Column
    private String profileimage;

    @Column
    private String usercomment;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> postList;

    public Member(SignUpRequestDto requestDto,String image) {
        this.account = requestDto.getAccount();
        this.password = requestDto.getPassword();
        this.username = requestDto.getUsername();
        this.profileimage = image;
        this.usercomment = requestDto.getUsercomment();
    }

}
