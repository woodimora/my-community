package com.sparta.community.controller;

import com.sparta.community.dto.PostDetailDto;
import com.sparta.community.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post-form")
    public String postForm() {
        return "postForm";
    }

    @GetMapping("/posts/{id}")
    public String getPostDetail(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response, Model model) {
        //게시글 조회수를 위해 현재 쿠키에 담겨져 있는 게시글 id를 확인.
        String cookieName = "postId" + id;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    //게시글 정보 응답
                    model.addAttribute("post",postService.getPostDetails(id));
                    return "postDetails";
                }
            }
        }
        //쿠키에 존재하지 않으면 조회수 증가 및 쿠키 생성
        PostDetailDto detailDto = postService.updateViewCount(id);//조회수 증가
        Cookie createCookie = new Cookie(cookieName, "true");   //쿠키 생성
        createCookie.setMaxAge(60 * 60); // 쿠키 만료시간 1시간
        response.addCookie(createCookie);
        model.addAttribute("post",detailDto);

        return "postDetails";
    }
}
