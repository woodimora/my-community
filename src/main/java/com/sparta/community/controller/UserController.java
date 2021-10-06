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
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
//private Long id;
//    private String nickname;
//    private String email;
    @PostMapping("/user/register/kakao")
    public String registerBykakao(@RequestParam("id") Long id,
                                  @RequestParam("nickname") String nickname,
                                  @RequestParam("email") String email,
                                  @RequestParam("profileImage") String profileImage,
                                  HttpSession session) {

        String data = profileImage.split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);
        String fileName = "/profile/" + UUID.randomUUID() + ".png";
        String filePath = "." + fileName;
        System.out.println("filePath = " + filePath);
        try {

            BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(bufImg, "png", new File(filePath));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String test = "";
        System.out.println("id = " + id);
        System.out.println("nickname = " + nickname);
        System.out.println("email = " + email);
        System.out.println("profileImage = " + profileImage);
        kakaoUserService.registerKakaoUser(new KakaoUserInfoDto(id, nickname, email), fileName);
        return "redirect:/";
    }

}