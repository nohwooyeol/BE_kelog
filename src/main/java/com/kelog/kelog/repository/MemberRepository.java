package com.kelog.kelog.repository;

import com.kelog.kelog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByAccount(String account);
    boolean existsAllByAccount(String a);
    Member findByUsername(String username);
    Optional<Member> findByAccount(String account);

}


