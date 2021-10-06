package com.sparta.community.controller;

import com.sparta.community.dto.PostDetailDto;
import com.sparta.community.dto.PostRequestDto;
import com.sparta.community.dto.PostResponseDto;
import com.sparta.community.model.Post;
import com.sparta.community.model.UserRoleEnum;
import com.sparta.community.repository.PostRepository;
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

    @GetMapping("/api/posts/{id}")
    public PostDetailDto getPostDetail(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        //게시글 조회수를 위해 현재 쿠키에 담겨져 있는 게시글 id를 확인.
        String cookieName = "postId" + id;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    //게시글 정보 응답
                    return postService.getPostDetails(id);
                }
            }
        }

        //쿠키에 존재하지 않으면 조회수 증가 및 쿠키 생성
        PostDetailDto detailDto = postService.updateViewCount(id);  //조회수 증가
        Cookie createCookie = new Cookie(cookieName, "true");   //쿠키 생성
        createCookie.setMaxAge(60 * 60); // 쿠키 만료시간 1시간
        response.addCookie(createCookie);

        return detailDto;
    }

    @DeleteMapping("/api/posts/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails);

        return "success";
    }

    @PostMapping("/api/posts/edit/{id}")
    public String editPost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.editPost(id, requestDto, userDetails);

        return "success";
    }

}
