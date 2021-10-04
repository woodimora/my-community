package com.sparta.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    private String username;
    private String nickname;
    private String password;
    private String email;
    private String profileImage;

    public UserRequestDto(String username, String nickname, String password, String email, String profileImage) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
    }
}
