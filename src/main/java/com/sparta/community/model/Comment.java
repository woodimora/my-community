package com.sparta.community.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.community.dto.CommentRequestDto;
import com.sparta.community.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private Long masterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private final List<Comment> child = new ArrayList<>();

    public Comment(CommentRequestDto requestDto, UserDetailsImpl userDetails, Long postId) {
        this.masterId = postId;
        this.contents = requestDto.getContents();
        this.user = userDetails.getUser();
    }

    public void addComment(Comment child){
        this.masterId = child.masterId;
        this.child.add(child);
        child.updateParent(this);
    }

    private void updateParent(Comment comment) {
        this.parent = comment;
    }

    public void updatePost(Post post) {
        this.post = post;
    }

    public void updateComment(String contents) {
        this.contents = contents;
    }
}
