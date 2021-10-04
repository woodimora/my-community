package com.sparta.community.dto;

import com.sparta.community.model.User;

public class UserResponseDto {
    private String username;
    private String nickname;
    private String email;
    private String kakaoId;
    private String profileImage;


    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.kakaoId = user.getKakaoId();
        this.profileImage = user.getProfileImage();
    }
}
