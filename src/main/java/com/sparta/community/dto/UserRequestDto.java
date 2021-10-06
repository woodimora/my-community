package com.sparta.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    @Size(min = 4, max = 16, message = "아이디를 2~16자 사이로 입력해주세요.")
    private String username;
    @NotBlank(message = "별명을 입력해주세요.")
    @Size(min = 2, max = 10, message = "별명을 2~10자 사이로 입력해주세요.")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 4, max = 32, message = "비밀번호를 8 ~ 32자 사이로 입력해주세요.")
    private String password;
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;
    private String profileImage;

    public void updateProfileImageUrl(String fileName) {
        this.profileImage = fileName;
    }
}