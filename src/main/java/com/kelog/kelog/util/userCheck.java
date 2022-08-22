package com.kelog.kelog.util;

import com.kelog.kelog.domain.Member;
import com.kelog.kelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class userCheck {


    private final MemberRepository memberRepository;

//    멤버로 조회하기
    public boolean existMember(Member member) {
        return memberRepository.existsByAccount(member.getAccount());
    }

    //    멤버 아이디로 조회하기
    public boolean existMemberbyAccount(String account) {
        return memberRepository.existsByAccount(account);
    }

}
