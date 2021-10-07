package com.sparta.community.dto;

import com.sparta.community.model.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CommentDto {
    private Long id;
    private Long postId;
    private String nickname;
    private String profileImage;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long parentId;
    private final List<CommentDto> child = new ArrayList<>();
    private boolean deleted;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPost() == null ? null : comment.getPost().getId();
        this.nickname = comment.getUser().getNickname();
        this.profileImage = comment.getUser().getProfileImage();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.parentId = comment.getParent() == null ? null : comment.getParent().getId();
        if (comment.getChild().size() > 0) {
            for (int i = 0; i < comment.getChild().size(); i++) {
                this.child.add(new CommentDto(comment.getChild().get(i)));
            }
        }
        this.deleted = comment.isDeleted();
    }
}
