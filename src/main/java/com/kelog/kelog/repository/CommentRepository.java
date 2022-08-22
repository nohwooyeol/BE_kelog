package com.kelog.kelog.repository;

import com.kelog.kelog.domain.Comment;
import com.kelog.kelog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
