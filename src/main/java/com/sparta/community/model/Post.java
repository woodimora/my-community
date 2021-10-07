package com.sparta.community.model;

import com.sparta.community.dto.PostRequestDto;
import com.sparta.community.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String title;

    @NotNull
    private String contents;

    @OneToMany(mappedBy = "post")
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private final List<Heart> hearts = new ArrayList<>();

    @Column(columnDefinition = "integer default 0")
    @NotNull
    private int viewCount;
    @Column(columnDefinition = "integer default 0")
    @NotNull
    private int commentCount;
    @Column(columnDefinition = "integer default 0")
    @NotNull
    private int heartCount;

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        comment.updatePost(this);
        this.commentCount += 1;
    }

    public void updatePost(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void upCountCommentCount() {
        this.commentCount += 1;
    }

    public void downCountCommentCount() {
        this.commentCount -= 1;
    }

    public void updateUser(User user){
        this.user = user;
    }

    public void addHeart(Heart heart) {
        this.hearts.add(heart);
        this.heartCount += 1;
    }

    public void deleteHeart(Heart heart) {
        this.hearts.remove(heart);
        this.heartCount -= 1;
    }
}
