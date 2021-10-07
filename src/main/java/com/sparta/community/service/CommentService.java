package com.sparta.community.service;

import com.sparta.community.dto.CommentRequestDto;
import com.sparta.community.model.Comment;
import com.sparta.community.model.Post;
import com.sparta.community.model.User;
import com.sparta.community.repository.CommentRepository;
import com.sparta.community.repository.PostRepository;
import com.sparta.community.repository.UserRepository;
import com.sparta.community.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(UserRepository userRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void CommentAddPost(CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 불러올 수 없습니다.")
        );
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다.")
        );
        Comment comment = commentRepository.save(new Comment(requestDto, post.getId()));
        post.addComment(comment);
        user.addComment(comment);
    }

    @Transactional
    public void CommentAddComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 불러올 수 없습니다.")
        );
        Comment parent = commentRepository.findById(requestDto.getParentId()).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, parent.getMasterId()));
        parent.addComment(comment);
        user.addComment(comment);

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
        if(comment.getChild().size() > 0 ){
            comment.deleteComment();
        }
        else {
            commentRepository.delete(comment);
        }
    }
}
