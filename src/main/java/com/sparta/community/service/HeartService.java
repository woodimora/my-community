package com.sparta.community.service;

import com.sparta.community.dto.HeartRequestDto;
import com.sparta.community.model.Heart;
import com.sparta.community.model.Post;
import com.sparta.community.model.User;
import com.sparta.community.repository.HeartRepository;
import com.sparta.community.repository.PostRepository;
import com.sparta.community.repository.UserRepository;
import com.sparta.community.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public HeartService(HeartRepository heartRepository, UserRepository userRepository, PostRepository postRepository) {
        this.heartRepository = heartRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @Transactional
    public boolean liked(Long postId, HeartRequestDto requestDto, UserDetailsImpl userDetails){
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 정보를 찾을 수 없습니다.")
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글 존재하지 않습니다.")
        );
        if(requestDto.getIsLiked().equals("true")){
            Heart heart = heartRepository.findByPostIdAndUserId(post.getId(), user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시물의 좋아요 이력이 없습니다.")
            );
            user.deleteHeart(heart);
            post.deleteHeart(heart);
            heartRepository.delete(heart);
            return false;
        }
        else{
            Heart heart = heartRepository.save(new Heart(post, user));
            user.addHeart(heart);
            post.addHeart(heart);
            return true;
        }
    }
}
