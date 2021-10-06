package com.sparta.community.model;

import com.sparta.community.dto.KakaoUserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private String profileImage;

    @OneToMany(mappedBy = "user")
    private final List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<Comment> commentList = new ArrayList<>();

    public User(String username, String nickname, String password, String email, String profileImage) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
        this.role = UserRoleEnum.USER;
    }

    public User(KakaoUserInfoDto infoDto, String password, String profileImage) {
        this.kakaoId = infoDto.getId();
        this.email = infoDto.getEmail();
        this.password = password;
        this.nickname = infoDto.getNickname();
        this.profileImage = profileImage;
        this.role = UserRoleEnum.USER;
    }


    public void updateKakaoId(Long id) {
        this.kakaoId = id;
    }
}
