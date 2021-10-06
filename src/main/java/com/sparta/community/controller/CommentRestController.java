package com.sparta.community.controller;

import com.sparta.community.dto.CommentRequestDto;
import com.sparta.community.security.UserDetailsImpl;
import com.sparta.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentRestController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/comments")
    public String createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (requestDto.getParentId() == null) {
            commentService.CommentAddPost(requestDto, userDetails);
        } else {
            commentService.CommentAddComment(requestDto, userDetails);
        }
        return "redirect:/posts/" + requestDto.getPostId();
    }

    @PostMapping("/api/comments/{id}")
    public String editComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.editComment(id, requestDto, userDetails);
        return "success";
    }

    @DeleteMapping("/api/comments/{id}")
    public String deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(id, userDetails);
        return "success";
    }
}
