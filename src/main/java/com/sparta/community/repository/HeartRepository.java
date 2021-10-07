package com.sparta.community.repository;

import com.sparta.community.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByPostIdAndUserId(Long postId, Long UserId);
}
