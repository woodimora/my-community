package com.sparta.community.controller;

import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.dto.UserResponseDto;
import com.sparta.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/user/register")
    public String registerForm() {
        return "userRegisterForm";
    }

    @PostMapping("/user/register/dup-id")
    @ResponseBody
    public String dupCheckId(@RequestBody UserRequestDto requestDto) {
        if (userService.checkDupId(requestDto)) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/user/register/dup-nickname")
    @ResponseBody
    public String dupCheckNickname(@RequestBody UserRequestDto requestDto) {
        if (userService.checkDupNickname(requestDto)) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/user/register")
    public String register(@RequestBody UserRequestDto requestDto) {
        userService.registerUser(requestDto);
        return "redirect:/";
    }
}