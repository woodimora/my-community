package com.sparta.community.testdata;

import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.model.User;
import com.sparta.community.model.UserRoleEnum;
import com.sparta.community.repository.UserRepository;
import com.sparta.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class TestDataRunner implements ApplicationRunner {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User testUser1 = new User("member1","nickname" , passwordEncoder.encode("123"), "aaa@mail.com", "https://bulma.io/images/placeholders/128x128.png");
        userRepository.save(testUser1);
    }
}
