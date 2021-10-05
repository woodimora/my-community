package com.sparta.community.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.community.dto.KakaoUserInfoDto;
import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.service.KakaoUserService;
import com.sparta.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
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

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, Model model) throws JsonProcessingException {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoUserService.kakaoLogin(code);

        if (kakaoUserInfoDto.getNickname() == null) {
            model.addAttribute("user", kakaoUserInfoDto);
            return "nicknameCheck";
        }
        else{
            return "redirect:/";
        }
    }

    @PostMapping("/user/register/kakao")
    public String register(@RequestBody KakaoUserInfoDto infoDto) {
        kakaoUserService.registerKakaoUser(infoDto);
        return "redirect:/";
    }

}