package com.kelog.kelog.repository;

import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByMemberId(Member member);
}
