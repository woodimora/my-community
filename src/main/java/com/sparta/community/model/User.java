package com.sparta.community.model;

import com.sparta.community.dto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private String profileImage;

    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    public User(String username, String nickname, String password, String email, String profileImage) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.role = UserRoleEnum.USER;
    }

    public User(UserRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
        this.email = requestDto.getEmail();
        this.role = UserRoleEnum.USER;
    }
}
