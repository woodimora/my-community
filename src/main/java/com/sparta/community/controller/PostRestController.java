package com.sparta.community.controller;

import com.sparta.community.dto.PostDetailDto;
import com.sparta.community.dto.PostRequestDto;
import com.sparta.community.dto.PostResponseDto;
import com.sparta.community.model.Post;
import com.sparta.community.model.UserRoleEnum;
import com.sparta.community.security.UserDetailsImpl;
import com.sparta.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PostRestController {

    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public Page<PostResponseDto> getPosts(@RequestParam int page, @RequestParam int display) {
        PageRequest pageRequest = PageRequest.of(page, display, Sort.by("createdAt").descending());
        return postService.getAllPosts(pageRequest);
    }

    //게시글 작성
    @Secured(value = UserRoleEnum.Authority.USER)
    @PostMapping("/api/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.save(new Post(requestDto, userDetails));
    }

    @Secured(value = UserRoleEnum.Authority.USER)
    @DeleteMapping("/api/posts/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails);

        return "success";
    }

    @Secured(value = UserRoleEnum.Authority.USER)
    @PostMapping("/api/posts/edit/{id}")
    public String editPost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.editPost(id, requestDto, userDetails);

        return "success";
    }

}
