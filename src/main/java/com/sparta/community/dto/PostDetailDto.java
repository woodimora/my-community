package com.sparta.community.dto;

import com.sparta.community.model.Comment;
import com.sparta.community.model.Heart;
import com.sparta.community.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@NoArgsConstructor
public class PostDetailDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private Long kakaoId;
    private String profileImage;
    private String title;
    private String contents;
    private final List<CommentDto> commentList = new ArrayList<>();
    private int commentCount;
    private int viewCount;
    private int heartCount;
    private final List<Long> likedUserIdList = new ArrayList<>();
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
        if (post.getCommentList().size() > 0) {
            for (int i = 0; i < post.getCommentList().size(); i++) {
                this.commentList.add(new CommentDto(post.getCommentList().get(i)));
            }
        }
        this.commentCount = post.getCommentCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.heartCount = post.getHeartCount();
        if (post.getHearts().size() > 0) {
            for(int i = 0 ;i < post.getHearts().size(); i++){
                this.likedUserIdList.add(post.getHearts().get(i).getUser().getId());
            }
        }
    }
}
