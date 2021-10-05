package com.sparta.community.service;

import com.sparta.community.dto.PostDetailDto;
import com.sparta.community.dto.PostResponseDto;
import com.sparta.community.model.Post;
import com.sparta.community.repository.PostRepository;
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
        Post post = postRepository.getById(id);
        return new PostDetailDto(post);
    }

    @Transactional
    public PostDetailDto updateViewCount(Long id) {
        Post post = postRepository.getById(id);
        post.updateViewCount();
        return new PostDetailDto(post);
    }
}
