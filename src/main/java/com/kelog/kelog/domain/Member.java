package com.kelog.kelog.domain;

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
public class Member extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private Long password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String profileimage;

    @Column(nullable = false)
    private String usercomment;



}
