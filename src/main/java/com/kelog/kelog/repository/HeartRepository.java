package com.kelog.kelog.repository;


import com.kelog.kelog.domain.Heart;
import com.kelog.kelog.domain.Member;
import com.kelog.kelog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Long countAllByPost(Post post);

    Long countByMemberIdAndPost(Long memberId, Post post);



    @Transactional
    void deleteById(Long Id);


    Heart findByMemberIdAndPost(Long memberId, Post post);

    boolean existsAllByPostAndMemberId(Post post, Long memberid);
}
