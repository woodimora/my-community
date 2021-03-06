package com.sparta.community.service;

import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.dto.UserResponseDto;
import com.sparta.community.exception.CustomErrorException;
import com.sparta.community.model.User;
import com.sparta.community.repository.UserRepository;
import com.sparta.community.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkDupId(UserRequestDto requestDto, UserDetailsImpl userDetails) {
        String username = requestDto.getUsername();
        User found = userRepository.findByUsername(username).orElse(null);
        if (found == null) {
            return true;
        } else if (userDetails == null) {
            return false;
        } else if (userDetails.getUser().getId().equals(found.getId())) {
            return true;
        }
        return false;
    }


    public boolean checkDupNickname(UserRequestDto requestDto, UserDetailsImpl userDetails) {
        String nickname = requestDto.getNickname();
        User found = userRepository.findByNickname(nickname).orElse(null);
        if (found == null) {
            return true;
        } else if (userDetails == null) {
            return false;
        } else if (userDetails.getUser().getId().equals(found.getId())) {
            return true;
        }
        return false;
    }

    public void registerUser(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        User findByUsername = userRepository.findByUsername(username).orElse(null);
        if (findByUsername != null) {
            throw new CustomErrorException("????????? ???????????? ???????????? ???????????? ???????????????.");
        }
        String nickname = requestDto.getNickname();
        User findByNickname = userRepository.findByNickname(nickname).orElse(null);
        if (findByNickname != null) {
            throw new CustomErrorException("????????? ????????? ???????????? ???????????? ???????????????.");
        }
        String email = requestDto.getEmail();
        User findByEmail = userRepository.findByEmail(email).orElse(null);
        if (findByEmail != null) {
            throw new CustomErrorException("????????? ???????????? ???????????? ???????????? ???????????????.");
        }

        User user = new User(requestDto, passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
    }

    public UserResponseDto getUser(UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new CustomErrorException("????????? ????????? ???????????? ????????????.")
        );
        return new UserResponseDto(user);
    }

    @Transactional
    public User updateUser(UserRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new CustomErrorException("????????? ????????? ???????????? ????????????.")
        );
        if (requestDto.getPassword().equals("")) {
            user.updateUser(requestDto);
        } else {
            String password = passwordEncoder.encode(requestDto.getPassword());
            user.updateUserWithPassword(requestDto, password);
        }
        return user;
    }
}
