package com.sparta.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String email;
    private String profileImage;

    public void updateProfileImageUrl(String fileName) {
        this.profileImage = fileName;
    }
}