package com.sparta.community.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.community.dto.KakaoUserInfoDto;
import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.dto.UserResponseDto;
import com.sparta.community.security.UserDetailsImpl;
import com.sparta.community.service.KakaoUserService;
import com.sparta.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
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

    @GetMapping("/user/edit")
    public String editForm(@AuthenticationPrincipal UserDetailsImpl userDetails,Model model) {
        UserResponseDto responseDto = userService.getUser(userDetails);
        model.addAttribute("user", responseDto);
        return "userEditForm";
    }

    @PostMapping("/user/edit")
    public String editUser(@RequestParam("username") String username,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("profileImage") String profileImage,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String fileName;
        if(!profileImage.equals("https://bulma.io/images/placeholders/128x128.png")){
            fileName = saveProfileImage(profileImage);
        }
        else {
            fileName = profileImage;
        }
        UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, fileName);

        userService.updateUser(requestDto, userDetails);
        return "redirect:/";
    }

    @PostMapping("/user/register/dup-id")
    @ResponseBody
    public String dupCheckId(@RequestBody UserRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userService.checkDupId(requestDto, userDetails)) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/user/register/dup-nickname")
    @ResponseBody
    public String dupCheckNickname(@RequestBody UserRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userService.checkDupNickname(requestDto, userDetails)) {
            return "success";
        }
        return "fail";
    }

    @PostMapping("/user/register")
//    public String register(@RequestBody UserRequestDto requestDto) {
    public String register(@RequestParam("username") String username,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("profileImage") String profileImage
                           ) {
        String fileName;
        if(!profileImage.equals("https://bulma.io/images/placeholders/128x128.png")){
            fileName = saveProfileImage(profileImage);
        }
        else {
            fileName = profileImage;
        }
        UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, fileName);

//        System.out.println("requestDto = " + requestDto);
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
    public String registerBykakao(@RequestParam("id") Long id,
                                  @RequestParam("email") String email,
                                  @RequestParam("nickname") String nickname,
                                  @RequestParam("profileImage") String profileImage) {
        String fileName;
        if(!profileImage.equals("https://bulma.io/images/placeholders/128x128.png")){
            fileName = saveProfileImage(profileImage);
        }
        else {
            fileName = profileImage;
        }
        KakaoUserInfoDto infoDto = new KakaoUserInfoDto(id, nickname, email, fileName);
//        System.out.println("infoDto = " + infoDto);
        kakaoUserService.registerKakaoUser(infoDto);
        return "redirect:/";
    }

    private String saveProfileImage(String profileImage) {
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
        return fileName;
    }
}