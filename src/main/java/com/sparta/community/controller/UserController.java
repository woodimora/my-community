package com.sparta.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/user/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/user/register")
    public String registerForm() {
        return "userRegisterForm";
    }

    @PostMapping("/user/dup-id")
    public String dupCheckId() {
        return "userRegisterForm";
    }

    @PostMapping("/user/dup-nickname")
    public String dupCheckNickname() {
        return "userRegisterForm";
    }
}
