package com.sparta.community.model;

import com.sparta.community.dto.CommentRequestDto;
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
public class Comment extends Timestamped {
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

    @NotNull
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private final List<Comment> child = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    public Comment(CommentRequestDto requestDto, Long postId) {
        this.masterId = postId;
        this.contents = requestDto.getContents();
    }

    public void addComment(Comment child) {
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

    public void deleteComment() {
        this.contents = "사용자에 의해 삭제된 댓글입니다.";
        this.deleted = true;
    }

    public void deleteAll() {
        this.parent = null;
        this.post = null;
        if(this.child.size() > 0){
            for(Comment comment : this.child){
                comment.deleteAll();
            }
            this.child.clear();
        }
    }

    public void updateUser(User user) {
        this.user = user;
    }
}
