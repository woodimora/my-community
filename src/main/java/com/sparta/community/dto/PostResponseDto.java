package com.sparta.community.dto;

import com.sparta.community.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String nickname;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int viewCount;
    private int commentsCount;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.nickname = post.getUser().getNickname();
        this.profileImage = post.getUser().getProfileImage();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.viewCount = post.getViewCount();
        this.commentsCount = post.getCommentList().size();
    }
}
