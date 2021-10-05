package com.sparta.community.service;

import com.sparta.community.dto.PostDetailDto;
import com.sparta.community.dto.PostRequestDto;
import com.sparta.community.dto.PostResponseDto;
import com.sparta.community.model.Post;
import com.sparta.community.repository.PostRepository;
import com.sparta.community.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto save(Post post) {
        return new PostResponseDto(postRepository.save(post));
    }

    public Page<PostResponseDto> getAllPosts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest).map(PostResponseDto::new);
    }

    public PostDetailDto getPostDetails(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다.")
        );
        return new PostDetailDto(post);
    }

    @Transactional
    public PostDetailDto updateViewCount(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다.")
        );
        post.updateViewCount();
        return new PostDetailDto(post);
    }

    public void deletePost(Long id, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public void editPost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        post.updatePost(requestDto.getTitle(), requestDto.getContents());
    }

    public PostResponseDto getPost(Long id, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("권한 없는 접근입니다.");
        }
        return new PostResponseDto(post);
    }
}
