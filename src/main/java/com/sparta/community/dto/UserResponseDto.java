package com.sparta.community.dto;

import com.sparta.community.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private Long kakaoId;
    private String profileImage;


    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.kakaoId = user.getKakaoId();
        this.profileImage = user.getProfileImage();
    }
}
