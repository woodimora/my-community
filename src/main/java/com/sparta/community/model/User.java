package com.sparta.community.model;

import com.sparta.community.dto.KakaoUserInfoDto;
import com.sparta.community.dto.UserRequestDto;
import com.sparta.community.validator.UserValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private Long kakaoId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private String profileImage;

    @OneToMany(mappedBy = "user")
    private final List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<Heart> hearts = new ArrayList<>();

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

    public User(KakaoUserInfoDto infoDto, String password) {
        this.kakaoId = infoDto.getId();
        this.nickname = infoDto.getNickname();
        this.email = infoDto.getEmail();
        this.password = password;
        this.profileImage = infoDto.getProfileImage();
        this.role = UserRoleEnum.USER;
    }

    public User(UserRequestDto requestDto, String password) {
        UserValidator.validateUserInput(requestDto);
        this.username = requestDto.getUsername();
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.password = password;
        this.profileImage = requestDto.getProfileImage();
        this.role = UserRoleEnum.USER;
    }


    public void updateKakaoId(Long id) {
        this.kakaoId = id;
    }

    public void updateUserWithPassword(UserRequestDto requestDto, String password) {
        this.username = requestDto.getUsername();
        this.password = password;
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.profileImage = requestDto.getProfileImage();
    }

    public void updateUser(UserRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.nickname = requestDto.getNickname();
        this.email = requestDto.getEmail();
        this.profileImage = requestDto.getProfileImage();
    }

    public void addPost(Post post) {
        this.postList.add(post);
        post.updateUser(this);
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        comment.updateUser(this);
    }

    public void addHeart(Heart heart) {
        this.hearts.add(heart);
    }

    public void deleteHeart(Heart heart) {
        this.hearts.remove(heart);
    }
}
