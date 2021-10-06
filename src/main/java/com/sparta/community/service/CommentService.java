package com.sparta.community.service;

import com.sparta.community.dto.CommentRequestDto;
import com.sparta.community.model.Comment;
import com.sparta.community.model.Post;
import com.sparta.community.repository.CommentRepository;
import com.sparta.community.repository.PostRepository;
import com.sparta.community.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void CommentAddPost(CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다.")
        );
        Comment comment = commentRepository.save(new Comment(requestDto, userDetails, post.getId()));
        post.addComment(comment);
    }

    @Transactional
    public void CommentAddComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment parent = commentRepository.findById(requestDto.getParentId()).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, userDetails, parent.getMasterId()));
        parent.addComment(comment);

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("댓글의 게시글을 찾을 수 없습니다.")
        );
        post.upCountCommentCount();
    }

    @Transactional
    public void editComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        if (!comment.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        comment.updateComment(requestDto.getContents());
    }

    @Transactional
    public void deleteComment(Long id, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        if (!comment.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        Post post = postRepository.findById(comment.getMasterId()).orElseThrow(
                () -> new IllegalArgumentException("댓글의 게시글을 찾을 수 없습니다.")
        );
        post.downCountCommentCount();
        commentRepository.delete(comment);
    }
}
