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

    @Column (nullable = false)
    private String account;

    @Column (nullable = false)
    private String password;

    @Column (nullable = false)
    private String username;

    @Column (nullable = false)
    private String profileimage;

    @Column (nullable = false)
    private String usercomment;

//    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
//    private List<Posts> postList;

    public Member(SignUpRequestDto requestDto,String image) {
        this.account = requestDto.getAccount();
        this.password = requestDto.getPassword();
        this.username = requestDto.getUsername();
        this.profileimage = image;
        this.usercomment = requestDto.getUsercomment();
    }

}
