package com.kelog.kelog.util;

import com.kelog.kelog.domain.Comment;
import com.kelog.kelog.domain.Post;
import com.kelog.kelog.repository.CommentRepository;
import com.kelog.kelog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckUtill {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true) //게시글이 있는지 확인 
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional(readOnly = true) //댓글이 있는지 확인
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }


}
