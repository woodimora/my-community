package com.sparta.community.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.community.dto.PostRequestDto;
import com.sparta.community.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int viewCount;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int commentCount;

    public Post(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        this.user = userDetails.getUser();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }

    public void updateCommentCount() {
        this.commentCount += 1;
    }
}
