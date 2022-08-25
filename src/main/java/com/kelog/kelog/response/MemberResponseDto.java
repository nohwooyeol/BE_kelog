package com.kelog.kelog.response;


import com.kelog.kelog.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String account;
    private String username;
    private String profileImg;
    private String userComment;


    public MemberResponseDto(Member member) {
        this.memberId = member.getId();
        this.account = member.getAccount();
        this.username = member.getUsername();
        this.profileImg = member.getProfileimage();
        this.userComment = member.getUsercomment();
    }
}
