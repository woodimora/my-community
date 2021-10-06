package com.sparta.community.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostRequestDto {
    @NotBlank(message = "게시글 제목을 입력하세요.")
    private String title;
    @NotBlank(message = "게시글 내용을 입력하세요.")
    private String contents;
}
