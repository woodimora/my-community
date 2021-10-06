package com.sparta.community.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.community.dto.KakaoUserInfoDto;
import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.dto.UserResponseDto;
import com.sparta.community.model.User;
import com.sparta.community.model.UserRoleEnum;
import com.sparta.community.security.UserDetailsImpl;
import com.sparta.community.service.KakaoUserService;
import com.sparta.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/login")
    public String loginError(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null)
        {
            return "alreadyLogin";
        }
        return "loginNecessary";
    }


    @GetMapping("/user/login")
    public String loginForm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null)     //로그인이 되어있는 경우
        {
            return "alreadyLogin";
        }
        return "loginForm";
    }

    @GetMapping("/user/register")
    public String registerForm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails != null)
        {
            return "alreadyLogin";
        }
        return "userRegisterForm";
    }

    @Secured(value = UserRoleEnum.Authority.USER)
    @GetMapping("/user/edit")
    public String editForm(@AuthenticationPrincipal UserDetailsImpl userDetails,Model model) {
        UserResponseDto responseDto = userService.getUser(userDetails);
        model.addAttribute("user", responseDto);
        return "userEditForm";
    }

    @Secured(value = UserRoleEnum.Authority.USER)
    @PostMapping("/user/edit")
    public String editUser(@RequestBody UserRequestDto requestDto,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String fileName = getFileName(requestDto.getProfileImage());
        requestDto.updateProfileImageUrl(fileName);
        User user = userService.updateUser(requestDto, userDetails);
        forceLogin(user);
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
    public String register(@RequestBody UserRequestDto requestDto) {
        String fileName = getFileName(requestDto.getProfileImage());
        requestDto.updateProfileImageUrl(fileName);
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
    public String registerBykakao(@RequestBody KakaoUserInfoDto infoDto) {
        String fileName = getFileName(infoDto.getProfileImage());
        infoDto.updateProfileImageUrl(fileName);
        kakaoUserService.registerKakaoUser(infoDto);
        return "redirect:/";
    }

    private String getFileName(String profileImage) {
        return saveProfileImage(profileImage);
    }

    private String saveProfileImage(String profileImage) {
        String fileName;
        try{
            String data = profileImage.split(",")[1];
            byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);
            fileName = "/profile/" + UUID.randomUUID() + ".png";
            String filePath = "." + fileName;
            System.out.println("filePath = " + filePath);
            try {
                BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));
                ImageIO.write(bufImg, "png", new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            fileName = profileImage;
        }
        return fileName;
    }

    private void forceLogin(User user) {
        // 4. 강제 로그인 처리
        UserDetails userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}