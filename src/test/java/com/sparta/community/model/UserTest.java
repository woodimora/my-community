package com.sparta.community.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Nested
    @DisplayName("회원등록")
    class RegisterUser {
        private String username;
        private String nickname;
        private String password;
        private String email;
        private String profileImage;

        @BeforeEach
        void setup() {
            username = "user1";
            nickname = "꼬북칩";
            password = "password";
            email = "email@mail.com";
            profileImage = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
        }

        //given
        @Test
        @DisplayName("회원등록 - 정상")
        void registerTest1() {
            User user = new User(username,nickname,password,email,profileImage);

            assertThat(user.getId()).isNull();
            assertThat(user.getUsername()).isEqualTo(username);
            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getProfileImage()).isEqualTo(profileImage);
        }
        //when
        @Nested
        @DisplayName("실패 케이스")
        class FailCases{

            @Nested
            @DisplayName("회원 username")
            class username {
                @Test
                @DisplayName("null")
                void usernameNull(){
                    //given
                    username = null;
                    User user = new User(username,nickname,password,email,profileImage);
                    //when

                    //then
                    assertThat(user.getId()).isNull();
                    assertThat(user.getUsername()).isNull();
                    assertThat(user.getNickname()).isEqualTo(nickname);
                    assertThat(user.getPassword()).isEqualTo(password);
                    assertThat(user.getEmail()).isEqualTo(email);
                    assertThat(user.getProfileImage()).isEqualTo(profileImage);
                }
            }
        }
        //then
    }
}
