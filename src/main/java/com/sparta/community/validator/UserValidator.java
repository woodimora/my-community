package com.sparta.community.validator;

import com.sparta.community.dto.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator {
    static String usernamePattern = "^(?=.*[a-zA-Z])[-a-zA-Z0-9_.]{4,16}$";
    static String nicknamePattern = "^[ㅏ-ㅣㄱ-ㅎ가-힣a-zA-Z0-9_.]{2,10}$";
    static String passwordPattern = "(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{4,32}$";
    static String emailPattern = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    public static void validateUserInput(UserRequestDto requestDto) {
        if ((requestDto.getUsername() != null) || !(requestDto.getUsername().equals(""))) {
            if (!Pattern.matches(usernamePattern, requestDto.getUsername())) {
                throw new IllegalArgumentException("회원 Id 가 유효하지 않습니다.");
            }
        }

        if (requestDto.getNickname() == null || requestDto.getNickname().equals("")) {
            throw new IllegalArgumentException("회원 별명은 필수 값입니다.");
        }
        if (!Pattern.matches(nicknamePattern, requestDto.getNickname())) {
            throw new IllegalArgumentException("회원 별명이 유효하지 않습니다.");
        }

        if (requestDto.getPassword() == null || requestDto.getPassword().equals("")) {
            throw new IllegalArgumentException("회원 비밀번호는 필수 값입니다.");
        }
        if (!Pattern.matches(passwordPattern, requestDto.getPassword())) {
            throw new IllegalArgumentException("회원 비밀번호가 유효하지 않습니다.");
        }

        if (requestDto.getEmail() != null || !requestDto.getEmail().equals("")) {
            if (!Pattern.matches(emailPattern, requestDto.getEmail())) {
                throw new IllegalArgumentException("회원 Email이 유효하지 않습니다.");
            }
        }
    }
}
