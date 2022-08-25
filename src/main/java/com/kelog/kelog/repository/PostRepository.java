package com.kelog.kelog.repository;

import com.kelog.kelog.domain.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAndTitleContainsOrContentContainsOrderByCreatedAtDesc(String title, String content, Pageable pageable);

    // 내 게시물 조회
    @Query("select DISTINCT p from Post p join fetch p.tags t join fetch p.member m  where p.member.id = :memberId")
    List<Post> findAllMyPage(Long memberId);

    // 전체조회
    @Query("select p from Post p join fetch p.member")
    List<Post> findAllByPaging(PageRequest createdAt);
    List<Post> findAllByPostId(Long postId, Pageable pageable);


    @Query("select p from Post p join fetch p.member")
    List<Post> findAllByTodayContents(PageRequest createdAt);


    List<Post> findAllByCreatedAtOrderByHeartCountDesc(LocalDate localDate, Pageable pageable);

    List<Post> findAllByCreatedAtGreaterThanOrderByHeartCountDesc(LocalDate localDate, Pageable pageable);

    // 회원 게시물 조회
    @Query("select p from Post p " +
            "join fetch p.member m " +
            "where p.member.id = :memberId  "
             )
    List<Post> findAllMemberId(Long memberId, Pageable pageable);



    Optional<Post> findByPostId(Long postid);

}
