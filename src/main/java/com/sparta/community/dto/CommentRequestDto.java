package com.sparta.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private Long parentId;
    @NotBlank(message = "댓글 내용을 입력하세요.")
    private String contents;
}
