package com.sparta.community.dto;

import com.sparta.community.model.Comment;
import com.sparta.community.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class PostDetailDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private Long kakaoId;
    private String profileImage;
    private String title;
    private String contents;
    private List<Comment> commentList = new ArrayList<>();
    private int commnetCount;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostDetailDto(Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.nickname = post.getUser().getNickname();
        this.email = post.getUser().getEmail();
        this.kakaoId = post.getUser().getKakaoId();
        this.profileImage = post.getUser().getProfileImage();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.commentList = post.getCommentList();
        this.commnetCount = commentList.size();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
