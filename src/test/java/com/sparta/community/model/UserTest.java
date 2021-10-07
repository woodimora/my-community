package com.sparta.community.model;

import com.sparta.community.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        @Test
        @DisplayName("회원등록 - 정상")
        void registerTest1() {
            User user = new User(username, nickname, password, email, profileImage);

            assertThat(user.getId()).isNull();
            assertThat(user.getUsername()).isEqualTo(username);
            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getProfileImage()).isEqualTo(profileImage);
        }

        @Test
        @DisplayName("Username null - 정상")
        void usernameNull() {
            //given
            username = null;
            User user = new User(username, nickname, password, email, profileImage);
            //when

            //then
            assertThat(user.getId()).isNull();
            assertThat(user.getUsername()).isNull();
            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getProfileImage()).isEqualTo(profileImage);
        }

        @Test
        @DisplayName("email null - 정상")
        void emailNull() {
            //given
            email = null;
            User user = new User(username, nickname, password, email, profileImage);
            //when

            //then
            assertThat(user.getId()).isNull();
            assertThat(user.getUsername()).isEqualTo(username);
            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getEmail()).isNull();
            assertThat(user.getProfileImage()).isEqualTo(profileImage);
        }
        @Nested
        @DisplayName("실패 케이스")
        class FailCases {
            @Nested
            @DisplayName("회원 username")
            class username{
                @Test
                @DisplayName("공백")
                void username공백에러(){
                    //given
                    username = "user 1";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);
                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 Id 가 유효하지 않습니다.");
                }

                @Test
                @DisplayName("한글포함")
                void username한글(){
                    //given
                    username = "user일";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);
                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 Id 가 유효하지 않습니다.");
                }

                @Test
                @DisplayName("글자수 부족")
                void username글자수부족(){
                    //given
                    username = "us";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);
                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 Id 가 유효하지 않습니다.");
                }

                @Test
                @DisplayName("글자수 초과")
                void username글자수초과(){
                    //given
                    username = "user1user2user3user4";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);
                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 Id 가 유효하지 않습니다.");
                }

                @Test
                @DisplayName("특수문자")
                void username특수문자(){
                    //given
                    username = "#@%#user1";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);
                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 Id 가 유효하지 않습니다.");
                }

                @Test
                @DisplayName("숫자만")
                void username숫자만(){
                    //given
                    username = "12344";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);
                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 Id 가 유효하지 않습니다.");
                }
            }

            @Nested
            @DisplayName("회원 nickname")
            class nickname {
                @Test
                @DisplayName("null")
                void nicknameNull(){
                    //given
                    nickname = null;

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);

                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 별명은 필수 값입니다.");
                }

                @Test
                @DisplayName("공백포함")
                void nickname공백(){
                    //given
                    nickname = "nickname 1";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);

                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 별명이 유효하지 않습니다.");
                }

                @Test
                @DisplayName("특수문자")
                void nickname특수문자(){
                    //given
                    nickname = "nickname#1";

                    UserRequestDto requestDto = new UserRequestDto(username, nickname, password, email, profileImage);

                    //when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new User(requestDto, password);
                    });
                    //then
                    assertThat(exception.getMessage()).isEqualTo("회원 별명이 유효하지 않습니다.");
                }
            }
        }
    }
}
