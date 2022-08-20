package com.kelog.kelog.repository;


import com.kelog.kelog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
