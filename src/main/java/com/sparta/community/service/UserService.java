package com.sparta.community.service;

import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.model.User;
import com.sparta.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkDupId(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        User found = userRepository.findByUsername(username).orElse(null);
        if (found == null) {
            return true;
        }
        return false;
    }


    public boolean checkDupNickname(UserRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        User found = userRepository.findByNickname(nickname).orElse(null);
        if (found == null) {
            return true;
        }
        return false;
    }

    public void registerUser(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        User findByUsername = userRepository.findByUsername(username).orElse(null);
        if (findByUsername != null) {
            throw new IllegalArgumentException("중복된 아이디를 사용하는 사용자가 존재합니다.");
        }
        String nickname = requestDto.getNickname();
        User findByNickname = userRepository.findByNickname(nickname).orElse(null);
        if (findByNickname != null) {
            throw new IllegalArgumentException("중복된 별명를 사용하는 사용자가 존재합니다.");
        }
        String email = requestDto.getEmail();
        User findByEmail = userRepository.findByEmail(email).orElse(null);
        if (findByEmail != null) {
            throw new IllegalArgumentException("중복된 이메일를 사용하는 사용자가 존재합니다.");
        }

        User user = new User(username, nickname, passwordEncoder.encode(requestDto.getPassword()), requestDto.getEmail(), requestDto.getProfileImage());
        userRepository.save(user);
    }
}
